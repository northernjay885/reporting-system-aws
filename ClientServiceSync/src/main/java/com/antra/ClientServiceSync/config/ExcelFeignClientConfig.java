package com.antra.ClientServiceSync.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;



public class ExcelFeignClientConfig {

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptorForExcel(@Value("${com.antra.excel.service.username}") String excelServiceUserName,
                                                                   @Value("${com.antra.excel.service.password}") String excelServicePassword) {
        return new BasicAuthRequestInterceptor(excelServiceUserName, excelServicePassword);
    }
}
