package com.antra.evaluation.reporting_system.endpoint;

import com.antra.evaluation.reporting_system.pojo.api.PDFRequest;
import com.antra.evaluation.reporting_system.pojo.api.PDFResponse;
import com.antra.evaluation.reporting_system.pojo.api.PDFSNSRequest;
import com.antra.evaluation.reporting_system.pojo.report.PDFFile;
import com.antra.evaluation.reporting_system.service.PDFService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class PDFRequestQueueListener {

    private static final Logger log = LoggerFactory.getLogger(PDFRequestQueueListener.class);

    private final QueueMessagingTemplate queueMessagingTemplate;

    private final PDFService pdfService;

    public PDFRequestQueueListener(QueueMessagingTemplate queueMessagingTemplate, PDFService pdfService) {
        this.queueMessagingTemplate = queueMessagingTemplate;
        this.pdfService = pdfService;
    }

   // @SqsListener("PDF_Request_Queue")
    public void queueListener(PDFRequest request) {
//        log.info("Get request: {}", request);
        PDFFile file = null;
        PDFResponse response = new PDFResponse();
        response.setReqId(request.getReqId());

        try {
            file = pdfService.createPDF(request);
            response.setFileId(file.getId());
            response.setFileLocation(file.getFileLocation());
            response.setFileSize(file.getFileSize());
            log.info("Generated: {}", file);

        } catch (Exception e) {
            response.setFailed(true);
            log.error("Error in generating pdf", e);
        }

        send(response);
        log.info("Replied back: {}", response);
    }

    @SqsListener("PDF_Request_Queue")
    public void fanoutQueueListener(PDFSNSRequest request) {
        log.info("Get fanout request: {}", request);
        queueListener(request.getPdfRequest());
    }

    private void send(Object message) {
        queueMessagingTemplate.convertAndSend("PDF_Response_Queue", message);
    }
}
/**
 * {
 *   "description":"Student Math Course Report",
 *   "headers":["Student #","Name","Class","Score"],
 *   "submitter":"Mrs. York1234"
 * }
 **/
