package com.antra.ClientServiceSync.service;

import com.antra.ClientServiceSync.pojo.reponse.PDFResponse;
import com.antra.ClientServiceSync.pojo.request.ReportRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="pdf-service")
public interface PDFServiceFeignClient {
    @RequestMapping(method= RequestMethod.POST, value="/pdf")
    PDFResponse getPDFServiceResponse(ReportRequest request);
}
