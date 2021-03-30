package com.antra.ClientServiceSync.service;

import com.amazonaws.services.s3.AmazonS3;
import com.antra.ClientServiceSync.entity.*;
import com.antra.ClientServiceSync.exception.RequestNotFoundException;
import com.antra.ClientServiceSync.pojo.FileType;
import com.antra.ClientServiceSync.pojo.reponse.ExcelResponse;
import com.antra.ClientServiceSync.pojo.reponse.PDFResponse;
import com.antra.ClientServiceSync.pojo.reponse.ReportVO;
import com.antra.ClientServiceSync.pojo.reponse.SqsResponse;
import com.antra.ClientServiceSync.pojo.request.ReportRequest;
import com.antra.ClientServiceSync.repository.ReportRequestRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
    private static final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final ReportRequestRepo reportRequestRepo;
    private final AmazonS3 s3Client;
    private final ExcelServiceFeignClient excelServiceFeignClient;
    private final PDFServiceFeignClient pdfServiceFeignClient;
    ExecutorService executorService = Executors.newFixedThreadPool(2);
    ReentrantLock lock = new ReentrantLock();

    public ReportServiceImpl(ReportRequestRepo reportRequestRepo,
                             AmazonS3 s3Client,
                             ExcelServiceFeignClient excelServiceFeignClient,
                             PDFServiceFeignClient pdfServiceFeignClient) {
        this.reportRequestRepo = reportRequestRepo;
        this.s3Client = s3Client;
        this.excelServiceFeignClient = excelServiceFeignClient;
        this.pdfServiceFeignClient = pdfServiceFeignClient;
    }

    private ReportRequestEntity persistToLocal(ReportRequest request) {
        request.setReqId("Req-"+ UUID.randomUUID().toString());

        ReportRequestEntity entity = new ReportRequestEntity();
        entity.setReqId(request.getReqId());
        entity.setSubmitter(request.getSubmitter());
        entity.setDescription(request.getDescription());
        entity.setCreatedTime(LocalDateTime.now());

        PDFReportEntity pdfReport = new PDFReportEntity();
        pdfReport.setRequest(entity);
        pdfReport.setStatus(ReportStatus.PENDING);
        pdfReport.setCreatedTime(LocalDateTime.now());
        entity.setPdfReport(pdfReport);

        ExcelReportEntity excelReport = new ExcelReportEntity();
        BeanUtils.copyProperties(pdfReport, excelReport);
        entity.setExcelReport(excelReport);

        return reportRequestRepo.save(entity);
    }

    @Override
    public ReportVO generateReportsSync(ReportRequest request) {
        persistToLocal(request);
        sendDirectRequests(request);
        return new ReportVO(reportRequestRepo.findById(request.getReqId()).orElseThrow());
    }

    private void sendDirectRequests(ReportRequest request) {

        Runnable excelTask = () -> {
            ExcelResponse excelResponse = new ExcelResponse();
            try {
                excelResponse = excelServiceFeignClient.getExcelServiceResponse(request);
            } catch(Exception e){
                log.error("Excel Generation Error (Sync) : e", e);
                excelResponse.setReqId(request.getReqId());
                excelResponse.setFailed(true);
            } finally {
                lock.lock();
                try {
                    updateLocal(excelResponse);
                } catch (Exception es) {
                    log.error("failed to persist the excel record to local database");
                    es.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        };

        Runnable pdfTask = () -> {
            PDFResponse pdfResponse = new PDFResponse();
            try {
                pdfResponse = pdfServiceFeignClient.getPDFServiceResponse(request);
            } catch(Exception e){
                log.error("PDF Generation Error (Sync) : e", e);
                pdfResponse.setReqId(request.getReqId());
                pdfResponse.setFailed(true);
            } finally {
                lock.lock();
                try {
                    updateLocal(pdfResponse);
                } catch (Exception es) {
                    log.error("failed to persist the excel record to local database");
                    es.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        };

        executorService.execute(excelTask);
        executorService.execute(pdfTask);
        executorService.shutdown();
    }

    private void updateLocal(ExcelResponse excelResponse) {
        SqsResponse response = new SqsResponse();
        BeanUtils.copyProperties(excelResponse, response);
        updateAsyncExcelReport(response);
    }
    private void updateLocal(PDFResponse pdfResponse) {
        SqsResponse response = new SqsResponse();
        BeanUtils.copyProperties(pdfResponse, response);
        updateAsyncPDFReport(response);
    }


    @Override
    public void updateAsyncPDFReport(SqsResponse response) {
        setReportByType(response, FileType.PDF);
    }

    @Override
    public void updateAsyncExcelReport(SqsResponse response) {
        setReportByType(response, FileType.EXCEL);
    }

    private void setReportByType(SqsResponse response, FileType type) {
        ReportRequestEntity entity = reportRequestRepo.findById(response.getReqId()).orElseThrow(RequestNotFoundException::new);
        BaseReportEntity report;
        if (type == FileType.EXCEL) {
            report = entity.getExcelReport();
        } else if(type == FileType.PDF) {
            report = entity.getPdfReport();
        } else {
            throw new NoSuchElementException("the report type does not exist!");
        }
        report.setUpdatedTime(LocalDateTime.now());
        if (response.isFailed()) {
            report.setStatus(ReportStatus.FAILED);
        } else{
            report.setStatus(ReportStatus.COMPLETED);
            report.setFileId(response.getFileId());
            report.setFileLocation(response.getFileLocation());
            report.setFileSize(response.getFileSize());
        }
        entity.setUpdatedTime(LocalDateTime.now());
        reportRequestRepo.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportVO> getReportList() {
        return reportRequestRepo.findAll().stream()
                .map(ReportVO::new)
                .collect(Collectors.toList());
    }

    @Override
    public InputStream getFileBodyByReqId(String reqId, FileType type) {
        ReportRequestEntity entity = reportRequestRepo.findById(reqId).orElseThrow(RequestNotFoundException::new);
        if (type == FileType.PDF) {
            String fileLocation = entity.getPdfReport().getFileLocation(); // this location is s3 "bucket/key"
            String bucket = fileLocation.split("/")[0];
            String key = fileLocation.split("/")[1];
            return s3Client.getObject(bucket, key).getObjectContent();
        } else if (type == FileType.EXCEL) {
            String fileLocation = entity.getExcelReport().getFileLocation();
            String bucket = fileLocation.split("/")[0];
            String key = fileLocation.split("/")[1];
            return s3Client.getObject(bucket, key).getObjectContent();
        }
        return null;
    }
}
