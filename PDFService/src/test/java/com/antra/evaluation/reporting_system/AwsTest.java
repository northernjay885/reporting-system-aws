package com.antra.evaluation.reporting_system;

import com.amazonaws.services.s3.AmazonS3;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AwsTest {

    @Autowired
    AmazonS3 s3Client;

    @Value("${s3.bucket}")
    private String s3Bucket;

    @Test
    public void testS3Client() {
        System.out.println(s3Client);
    }

    @Test
    public void listAllObjects() {
        s3Client.listObjects(s3Bucket).getObjectSummaries().forEach(System.out::println);
    }
}
