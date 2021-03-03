package com.antra.report.client.service;

import com.antra.report.client.pojo.reponse.GeneralResponse;
import com.antra.report.client.pojo.request.ReportRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="client-service-sync")
public interface ReportServiceSyncFeignClient {

    @RequestMapping(method= RequestMethod.POST, value="/report/sync")
    ResponseEntity<GeneralResponse> generateReportsSync(ReportRequest request);
}
