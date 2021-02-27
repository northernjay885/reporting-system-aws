package com.antra.report.client.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.antra.report.client.pojo.request.ReportRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class SNSServiceImpl implements SNSService {

    private final NotificationMessagingTemplate notificationMessagingTemplate;

    @Value("${app.aws.sns.topic}")
    private String snsTopic;

    @Autowired
    public SNSServiceImpl(AmazonSNS amazonSns) {
        this.notificationMessagingTemplate = new NotificationMessagingTemplate(amazonSns);
    }

    private void send(Object message) {
        this.notificationMessagingTemplate.sendNotification(snsTopic, message, null);
    }

    @Override
    public void sendReportNotification(ReportRequest request) {
        send(request);
    }
}
