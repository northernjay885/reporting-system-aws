package com.antra.report.client.listener;

import com.antra.report.client.pojo.reponse.SqsResponse;
import com.antra.report.client.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class ReportSQSListener {

    private static final Logger log = LoggerFactory.getLogger(ReportSQSListener.class);

    private ReportService reportService;

    public ReportSQSListener(ReportService reportService) {
        this.reportService = reportService;
    }

    @SqsListener("PDF_Response_Queue")
    public void responseQueueListenerPdf(SqsResponse response) {
        log.info("Get response from sqs : {}", response);
        reportService.updateAsyncPDFReport(response);
    }

    @SqsListener("Excel_Response_Queue")
    public void responseQueueListenerExcel(SqsResponse response) {
        log.info("Get response from sqs : {}", response);
        reportService.updateAsyncExcelReport(response);
    }

}
