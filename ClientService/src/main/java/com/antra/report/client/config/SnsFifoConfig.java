package com.antra.report.client.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnsFifoConfig {

    @Value("${AWS_CREDENTIALS_ACCESS_KEY}")
    private String amazonAWSAccessKey;

    @Value("${AWS_CREDENTIALS_SECRET_KEY}")
    private String amazonAWSSecretKey;

    @Bean
    public SnsFifoMessageGroupIdHandler messageGroupIdRequestHandler() {
        return new SnsFifoMessageGroupIdHandler();
    }

    @Bean
    public AWSCredentials amazonAWSCredentials() {
        return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
    }

    @Bean
    public AmazonSNSAsync amazonSNS(SnsFifoMessageGroupIdHandler snsFifoMessageGroupIdHandler) {
        AmazonSNSAsyncClientBuilder amazonSNSAsyncClientBuilder = AmazonSNSAsyncClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(amazonAWSCredentials()))
                .withRegion(Regions.US_EAST_1)
                .withRequestHandlers(snsFifoMessageGroupIdHandler);
        return amazonSNSAsyncClientBuilder.build();
    }
}
