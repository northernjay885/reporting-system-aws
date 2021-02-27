package com.antra.report.client;

import com.antra.report.client.pojo.request.ReportRequest;
import com.antra.report.client.service.ReportService;
import com.antra.report.client.service.SNSService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestSNS {

    @Autowired
    SNSService snsService;

    @Autowired
    ReportService reportService;

    @Test
    public void testReportService() {
        ReportRequest request = new ReportRequest();
        request.setSubmitter("Dawei_test");
        request.setDescription("This is just a test");
        request.setHeaders(List.of("Id","Name","Age"));
        request.setData(List.of(List.of("1","Dd","23"),List.of("2","AJ","32")));
   //     reportService.generateReports(request);
    }
    @Test
    public void testSNSSend() {
        ReportRequest request = new ReportRequest();
        request.setSubmitter("Dawei_test");
        request.setDescription("This is just a test");
        request.setHeaders(List.of("Id","Name","Age"));
        request.setData(List.of(List.of("1","Dd","23"),List.of("2","AJ","32")));

  //      snsService.generateReport(request);
    }
}
