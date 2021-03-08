package com.antra.ClientServiceSync.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;


public class PDFFeignClientConfig {

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptorForPDF(@Value("${com.antra.pdf.service.username}") String pdfServiceUserName,
                                                                   @Value("${com.antra.pdf.service.password}") String pdfServicePassword) {
        return new BasicAuthRequestInterceptor(pdfServiceUserName, pdfServicePassword);
    }
}
