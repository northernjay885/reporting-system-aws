package com.antra.report.client.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor(@Value("${com.antra.client.service.sync.username}") String clientServiceSyncUserName,
                                                                   @Value("${com.antra.client.service.sync.password}") String clientServiceSyncPassword) {
        return new BasicAuthRequestInterceptor(clientServiceSyncUserName, clientServiceSyncPassword);
    }
}
