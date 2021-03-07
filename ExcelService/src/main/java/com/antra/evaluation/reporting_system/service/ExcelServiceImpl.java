package com.antra.evaluation.reporting_system.service;

import com.amazonaws.services.s3.AmazonS3;
import com.antra.evaluation.reporting_system.exception.FileGenerationException;
import com.antra.evaluation.reporting_system.pojo.request.ExcelRequest;
import com.antra.evaluation.reporting_system.pojo.request.MultiSheetExcelRequest;
import com.antra.evaluation.reporting_system.pojo.report.ExcelData;
import com.antra.evaluation.reporting_system.pojo.report.ExcelDataHeader;
import com.antra.evaluation.reporting_system.pojo.report.ExcelDataSheet;
import com.antra.evaluation.reporting_system.pojo.report.ExcelFile;
import com.antra.evaluation.reporting_system.repository.ExcelRepository;
import org.apache.commons.collections4.IterableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExcelServiceImpl implements ExcelService {

    private static final Logger log = LoggerFactory.getLogger(ExcelServiceImpl.class);

    private final ExcelRepository excelRepository;

    private final ExcelGenerationService excelGenerationService;

    private final AmazonS3 s3Client;

    @Value("${s3.bucket}")
    private String s3Bucket;


    public ExcelServiceImpl(ExcelRepository excelRepository, ExcelGenerationService excelGenerationService, AmazonS3 s3Client) {
        this.excelRepository = excelRepository;
        this.excelGenerationService = excelGenerationService;
        this.s3Client = s3Client;
    }

    @Override
    public InputStream getExcelBodyById(String id) throws FileNotFoundException {
        Optional<ExcelFile> fileInfo = excelRepository.findById(id);
        return new FileInputStream(fileInfo.orElseThrow(FileNotFoundException::new).getFileLocation());
    }

    @Override
    public ExcelFile generateFile(ExcelRequest request, boolean multiSheet) {

        ExcelFile fileInfo = new ExcelFile();
        fileInfo.setFileId(UUID.randomUUID().toString());

        ExcelData data = new ExcelData();
        data.setTitle(request.getDescription());
        data.setFileId(fileInfo.getFileId());
        data.setSubmitter(fileInfo.getSubmitter());

        if(multiSheet){
            data.setSheets(generateMultiSheet(request));
        }else {
            data.setSheets(generateSheet(request));
        }

        try {
            File generatedFile = excelGenerationService.generateExcelReport(data);

            log.debug("Upload excel file to s3 {}", generatedFile.getAbsolutePath());
            s3Client.putObject(s3Bucket, generatedFile.getName(), generatedFile);
            log.debug("Uploaded");

            fileInfo.setFileLocation(String.join("/", s3Bucket, generatedFile.getName()));
            fileInfo.setFileName(generatedFile.getName());
            fileInfo.setGeneratedTime(LocalDateTime.now().toString());
            fileInfo.setSubmitter(request.getSubmitter());
            fileInfo.setFileSize(generatedFile.length());
            fileInfo.setDescription(request.getDescription());

            if(generatedFile.delete()) {
                log.debug("deleted local files!");
            }

        } catch (IOException e) {
            throw new FileGenerationException(e);
        }

        excelRepository.save(fileInfo);
        log.info("Excel File Generated : {}", fileInfo);

        return fileInfo;
    }

    @Override
    public List<ExcelFile> getExcelList() {
        return IterableUtils.toList(excelRepository.findAll());
    }

    @Override
    public ExcelFile deleteFile(String id) throws FileNotFoundException {
        Optional<ExcelFile> excelFileOpt = excelRepository.findById(id);
        if (excelFileOpt.isEmpty()) {
            throw new FileNotFoundException();
        }
        File file = new File(excelFileOpt.get().getFileLocation());
        file.delete();
        excelRepository.deleteById(id);
        return excelFileOpt.get();
    }

    private List<ExcelDataSheet> generateSheet(ExcelRequest request) {
        List<ExcelDataSheet> sheets = new ArrayList<>();
        ExcelDataSheet sheet = new ExcelDataSheet();

        sheet.setHeaders(request.getHeaders().stream()
                .map(ExcelDataHeader::new)
                .collect(Collectors.toList()));
        sheet.setDataRows(request.getData().stream()
                .map(listOfString -> (List<Object>) new ArrayList<Object>(listOfString))
                .collect(Collectors.toList()));
        sheet.setTitle("sheet-1");

        sheets.add(sheet);

        return sheets;
    }
    private List<ExcelDataSheet> generateMultiSheet(ExcelRequest request) {
        List<ExcelDataSheet> sheets = new ArrayList<>();
        int index = request.getHeaders().indexOf(((MultiSheetExcelRequest) request).getSplitBy());

        Map<String, List<List<String>>> splitData = request.getData().stream()
                .collect(Collectors.groupingBy(row -> row.get(index)));
        List<ExcelDataHeader> headers = request.getHeaders().stream()
                .map(ExcelDataHeader::new).collect(Collectors.toList());

        splitData.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()).forEach(
                entry ->{
                    ExcelDataSheet sheet = new ExcelDataSheet();
                    sheet.setHeaders(headers);
                    sheet.setDataRows(entry.getValue().stream()
                            .map(listOfString -> {
                                return (List<Object>) new ArrayList<Object>(listOfString);
                            })
                            .collect(Collectors.toList()));
                    sheet.setTitle(entry.getKey());
                    sheets.add(sheet);
                }
        );

        return sheets;
    }
}
