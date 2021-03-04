package com.antra.evaluation.reporting_system.pojo.request;

import javax.validation.constraints.NotNull;

public class CrudRequest {
    @NotNull
    private String id;
    @NotNull
    private String crudType;
    @NotNull
    private String fileId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCrudType() {
        return crudType;
    }

    public void setCrudType(String crudType) {
        this.crudType = crudType;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    @Override
    public String toString() {
        return "CrudRequest{" +
                "id='" + id + '\'' +
                ", crudType='" + crudType + '\'' +
                ", fileId='" + fileId + '\'' +
                '}';
    }
}
