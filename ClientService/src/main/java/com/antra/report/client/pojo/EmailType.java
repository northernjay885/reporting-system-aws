package com.antra.report.client.pojo;

public enum EmailType {
    SUCCESS("Hi %NAME%, your report is generated.");

    public String content;

    EmailType(String content) {
        this.content = content;
    }
}
