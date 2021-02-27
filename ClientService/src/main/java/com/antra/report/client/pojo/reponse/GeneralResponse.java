package com.antra.report.client.pojo.reponse;

import org.springframework.http.HttpStatus;

public class GeneralResponse {
    private HttpStatus statusCode = HttpStatus.OK;
    private Object data;

    public GeneralResponse() {}

    public GeneralResponse(Object data) {
        this.data = data;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
