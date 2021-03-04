package com.antra.report.client.config;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.handlers.RequestHandler2;
import com.amazonaws.services.sns.model.PublishRequest;

import java.util.UUID;

public class SnsFifoMessageGroupIdHandler extends RequestHandler2 {

    @Override
    public AmazonWebServiceRequest beforeExecution(AmazonWebServiceRequest request) {
        AmazonWebServiceRequest amazonWebServiceRequest = super.beforeExecution(request);
        if (amazonWebServiceRequest instanceof PublishRequest) {
            PublishRequest publishRequest = (PublishRequest) amazonWebServiceRequest;
            if (publishRequest.getTopicArn().contains(".fifo")) {
                publishRequest.setMessageGroupId(publishRequest.getTopicArn());
                publishRequest.setMessageDeduplicationId(UUID.randomUUID().toString());
            }
        }
        return amazonWebServiceRequest;
    }

}
