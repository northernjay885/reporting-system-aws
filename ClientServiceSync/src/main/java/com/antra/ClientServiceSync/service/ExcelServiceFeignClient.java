package com.antra.ClientServiceSync.service;

import com.antra.ClientServiceSync.config.ExcelFeignClientConfig;
import com.antra.ClientServiceSync.pojo.reponse.ExcelResponse;
import com.antra.ClientServiceSync.pojo.request.ReportRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="excel-service", configuration = ExcelFeignClientConfig.class)
public interface ExcelServiceFeignClient {
    @RequestMapping(method= RequestMethod.POST, value="/excel")
    ExcelResponse getExcelServiceResponse(ReportRequest request);
}
