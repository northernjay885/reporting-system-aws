package com.antra.report.client.pojo.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import java.util.List;

public class ReportRequest {
    @Null // this field will be set in the service. shouldn't be passed from client
    private String reqId;
    @NotEmpty
    private List<String> headers;
    @NotBlank
    private String description;
    @NotEmpty
    private List<List<String>> data;
    @NotBlank
    private String submitter;

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<List<String>> getData() {
        return data;
    }

    public void setData(List<List<String>> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ReportRequest{" +
                "headers=" + headers +
                ", description='" + description + '\'' +
                ", data=" + data +
                ", submitter='" + submitter + '\'' +
                '}';
    }
}
