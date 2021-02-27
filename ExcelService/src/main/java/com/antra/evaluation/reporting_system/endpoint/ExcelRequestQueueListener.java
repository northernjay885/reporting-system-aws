package com.antra.evaluation.reporting_system.endpoint;

import com.antra.evaluation.reporting_system.pojo.api.ExcelRequest;
import com.antra.evaluation.reporting_system.pojo.api.ExcelResponse;
import com.antra.evaluation.reporting_system.pojo.api.ExcelSNSRequest;
import com.antra.evaluation.reporting_system.pojo.report.ExcelFile;
import com.antra.evaluation.reporting_system.service.ExcelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class ExcelRequestQueueListener {

    private static final Logger log = LoggerFactory.getLogger(ExcelRequestQueueListener.class);

    private final QueueMessagingTemplate queueMessagingTemplate;

    private final ExcelService excelService;

    public ExcelRequestQueueListener(QueueMessagingTemplate queueMessagingTemplate, ExcelService excelService) {
        this.queueMessagingTemplate = queueMessagingTemplate;
        this.excelService = excelService;
    }

   // @SqsListener("PDF_Request_Queue")
    public void queueListener(ExcelRequest request) {
//        log.info("Get request: {}", request);
        ExcelFile file = null;
        ExcelResponse response = new ExcelResponse();
        response.setReqId(request.getReqId());

        try {
            file = excelService.generateFile(request, false);
            response.setFileId(file.getFileId());
            response.setFileLocation(file.getFileLocation());
            response.setFileSize(file.getFileSize());
            log.info("Generated: {}", file);

        } catch (Exception e) {
            response.setFailed(true);
            log.error("Error in generating excel", e);
        }

        send(response);
        log.info("Replied back: {}", response);
    }

    @SqsListener("Excel_Request_Queue")
    public void fanoutQueueListener(ExcelSNSRequest request) {
        log.info("Get fanout request: {}", request);
        queueListener(request.getExcelRequest());
    }

    private void send(Object message) {
        queueMessagingTemplate.convertAndSend("Excel_Response_Queue", message);
    }
}
/**
 * {
 *   "description":"Student Math Course Report",
 *   "headers":["Student #","Name","Class","Score"],
 *   "submitter":"Mrs. York1234"
 * }
 **/
