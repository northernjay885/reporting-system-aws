package com.antra.report.client.service;

import com.amazonaws.services.s3.AmazonS3;
import com.antra.report.client.entity.*;
import com.antra.report.client.exception.RequestNotFoundException;
import com.antra.report.client.pojo.EmailType;
import com.antra.report.client.pojo.FileType;
import com.antra.report.client.pojo.reponse.*;
import com.antra.report.client.pojo.request.CrudRequest;
import com.antra.report.client.repository.ReportRequestRepo;
import com.antra.report.client.pojo.request.ReportRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
    private static final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final ReportServiceSyncFeignClient feignClient;
    private final ReportRequestRepo reportRequestRepo;
    private final SnsReportingService snsReportingService;
    private final SnsCrudService snsCrudService;
    private final AmazonS3 s3Client;
    private final EmailService emailService;

    public ReportServiceImpl(ReportServiceSyncFeignClient feignClient,
                             ReportRequestRepo reportRequestRepo,
                             SnsReportingService snsReportingService,
                             SnsCrudService snsCrudService,
                             AmazonS3 s3Client,
                             EmailService emailService) {
        this.feignClient = feignClient;
        this.reportRequestRepo = reportRequestRepo;
        this.snsReportingService = snsReportingService;
        this.snsCrudService = snsCrudService;
        this.s3Client = s3Client;
        this.emailService = emailService;
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
    public ResponseEntity<GeneralResponse> generateReportsSync(ReportRequest request) {
        return feignClient.generateReportsSync(request);
    }

    @Override
    @Transactional
    public ReportVO generateReportsAsync(ReportRequest request) {
        ReportRequestEntity entity = persistToLocal(request);
        snsReportingService.sendReportNotification(request);
        log.info("Send SNS the message: {}",request);
        return new ReportVO(entity);
    }

    @Override
    @Transactional
    public void updateAsyncPDFReport(SqsResponse response) {
        setReportByType(response, FileType.PDF);
    }

    @Override
    @Transactional
    public void updateAsyncExcelReport(SqsResponse response) {
        setReportByType(response, FileType.EXCEL);
    }

    void setReportByType(SqsResponse response, FileType type) {
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
        log.debug("Save method will be called!");
        reportRequestRepo.save(entity);
        String to = "lrj193927@gmail.com";
        emailService.sendEmail(to, EmailType.SUCCESS, entity.getSubmitter());
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

    @Override
    @Transactional
    public void deleteReportByReqId(String reqId) {
        ReportRequestEntity entity = reportRequestRepo.findById(reqId).orElseThrow(RequestNotFoundException::new);

        FileType[] types = new FileType[] {FileType.EXCEL, FileType.PDF};
        for (FileType type : types) {
            String fileLocation;
            if (type == FileType.PDF) {
                fileLocation = entity.getPdfReport().getFileLocation();
            } else {
                fileLocation = entity.getExcelReport().getFileLocation();
            }
            if (fileLocation != null) {
                String bucket = fileLocation.split("/")[0];
                String key = fileLocation.split("/")[1];
                s3Client.deleteObject(bucket, key);
            }
        }

        CrudRequest request = new CrudRequest();
        request.setCrudType("DELETE");
        if (entity.getExcelReport().getFileId() != null) {
            request.setFileId(entity.getExcelReport().getFileId());
        } else {
            request.setFileId(entity.getPdfReport().getFileId());
        }
        request.setId(UUID.randomUUID().toString());

        snsCrudService.sendCrudNotification(request);
        log.debug("crud request with id: {} sent", request.getId());
        reportRequestRepo.deleteById(reqId);
    }

//    @Override
//    public void updateReportDetails(String reqId) {
//        ReportRequestEntity entity = reportRequestRepo.findById(reqId).orElseThrow(RequestNotFoundException::new);
//        //it seems the update feature is not necessary.
//    }
}
