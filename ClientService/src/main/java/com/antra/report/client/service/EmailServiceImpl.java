package com.antra.report.client.service;

import com.antra.report.client.pojo.EmailType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final QueueMessagingTemplate queueMessagingTemplate;

    public EmailServiceImpl(QueueMessagingTemplate queueMessagingTemplate) {
        this.queueMessagingTemplate = queueMessagingTemplate;
    }

    @Override
    public void sendEmail(String to, EmailType success, String submitter) {
        String emailTemplate = success.content;
        String emailBody = emailTemplate.replace("%NAME%", submitter);
        Email email = new Email();
        email.setTo(to);
        email.setFrom("do_not_reply@antra.com");
        email.setSubject("We did it!");
        email.setBody(emailBody);
        email.setToken("12345");
        send(email);
    }
    private void send(Object message) {
        queueMessagingTemplate.convertAndSend("email_queue", message);
        log.info("Email sent: {}", message);
        String s = "{ \"token\": \"12345\", \"to\": \"youremail@gmail.com\", \"subject\": \"This is a test\", \"body\": \"<html>Hi <b>Dawei!</b></html>\" }";
    }

}

class Email{
    private String to;
    private String from;
    private String subject;
    private String body;
    private String token; // because of lambda

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
