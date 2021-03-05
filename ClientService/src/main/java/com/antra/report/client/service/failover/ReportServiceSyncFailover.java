package com.antra.report.client.service.failover;

import com.antra.report.client.pojo.reponse.GeneralResponse;
import com.antra.report.client.pojo.request.ReportRequest;
import com.antra.report.client.service.ReportServiceSyncFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ReportServiceSyncFailover implements ReportServiceSyncFeignClient {
    private static final Logger log = LoggerFactory.getLogger(ReportServiceSyncFailover.class);

    @Override
    public ResponseEntity<GeneralResponse> generateReportsSync(ReportRequest request) {
        GeneralResponse response = new GeneralResponse();
        response.setStatusCode(HttpStatus.REQUEST_TIMEOUT);
        log.error("Something bad happened to the sync server!");
        return new ResponseEntity<>(response, HttpStatus.REQUEST_TIMEOUT);
    }
}
