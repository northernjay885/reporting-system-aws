package com.antra.evaluation.reporting_system.listener;

import com.antra.evaluation.reporting_system.pojo.request.ExcelRequest;
import com.antra.evaluation.reporting_system.pojo.response.ExcelResponse;
import com.antra.evaluation.reporting_system.pojo.request.ExcelSNSRequest;
import com.antra.evaluation.reporting_system.pojo.report.ExcelFile;
import com.antra.evaluation.reporting_system.service.ExcelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class ExcelRequestQueueListener {

    private static final Logger log = LoggerFactory.getLogger(ExcelRequestQueueListener.class);

    private final QueueMessagingTemplate queueMessagingTemplate;

    private final ExcelService excelService;

    @Value("${app.aws.sqs.excel.response.queue}")
    String excelResponseQueue;

    public ExcelRequestQueueListener(QueueMessagingTemplate queueMessagingTemplate, ExcelService excelService) {
        this.queueMessagingTemplate = queueMessagingTemplate;
        this.excelService = excelService;
    }

    public void queueListener(ExcelRequest request) {
        ExcelFile file;
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

    @SqsListener("${app.aws.sqs.task.queue.name}")
    public void fanoutQueueListener(ExcelSNSRequest request) {
        log.info("Got fanout request: {}", request);
        queueListener(request.getExcelRequest());
    }

    private void send(Object message) {
        queueMessagingTemplate.convertAndSend(excelResponseQueue, message);
    }
}

