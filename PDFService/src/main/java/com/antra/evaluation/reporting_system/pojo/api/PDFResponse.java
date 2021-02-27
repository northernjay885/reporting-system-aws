package com.antra.evaluation.reporting_system.pojo.api;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class PDFResponse {

    private String fileId;
    private String reqId;
    private String fileLocation;
    private long fileSize;
    private boolean failed;

    public PDFResponse() {
    }

//    public PDFResponse(String fileId) {
//        this.fileId = fileId;
//    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    @Override
    public String toString() {
        return "PDFResponse{" +
                "fileId='" + fileId + '\'' +
                ", reqId='" + reqId + '\'' +
                ", fileLocation='" + fileLocation + '\'' +
                ", fileSize=" + fileSize +
                ", failed=" + failed +
                '}';
    }
}
