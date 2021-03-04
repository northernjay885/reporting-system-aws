package com.antra.report.client.pojo.request;

import javax.validation.constraints.NotNull;

public class CrudRequest {

    @NotNull
    private String id;
    @NotNull
    private String crudType;

    private String excelFileId;

    private String pdfFileId;

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

    public String getExcelFileId() {
        return excelFileId;
    }

    public void setExcelFileId(String excelFileId) {
        this.excelFileId = excelFileId;
    }

    public String getPdfFileId() {
        return pdfFileId;
    }

    public void setPdfFileId(String pdfFileId) {
        this.pdfFileId = pdfFileId;
    }

    @Override
    public String toString() {
        return "CrudRequest{" +
                "id='" + id + '\'' +
                ", crudType='" + crudType + '\'' +
                ", excelFileId='" + excelFileId + '\'' +
                ", pdfFileId='" + pdfFileId + '\'' +
                '}';
    }
}
