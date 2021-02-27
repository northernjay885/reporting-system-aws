package com.antra.report.client.pojo.reponse;

import org.springframework.http.HttpStatus;

public class ErrorResponse extends GeneralResponse {
    private String message;

    public ErrorResponse(HttpStatus status, String message) {
        super.setStatusCode(status);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
