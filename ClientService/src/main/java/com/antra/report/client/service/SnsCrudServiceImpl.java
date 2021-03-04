package com.antra.report.client.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.antra.report.client.pojo.request.CrudRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class SnsCrudServiceImpl implements SnsCrudService {

    private final NotificationMessagingTemplate notificationMessagingTemplate;

    @Value("${app.aws.sns.crud.topic}")
    private String snsTopic;

    public SnsCrudServiceImpl(AmazonSNS amazonSns) {
        this.notificationMessagingTemplate = new NotificationMessagingTemplate(amazonSns);
    }

    private void send(Object message) {
        this.notificationMessagingTemplate.sendNotification(snsTopic, message, null);
    }

    @Override
    public void sendCrudNotification(CrudRequest crudRequest) {
        send(crudRequest);
    }
}
