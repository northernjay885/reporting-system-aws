package com.antra.report.client.pojo.reponse;

import com.antra.report.client.entity.ReportRequestEntity;
import com.antra.report.client.entity.ReportStatus;

import java.time.LocalDateTime;

public class ReportVO {
    private ReportRequestEntity entity;

    public ReportVO() {}

    public ReportVO(ReportRequestEntity entity) {
        this.entity = entity;
    }

    public String getSubmitter() {
        return entity.getSubmitter();
    }

    public String getId() {
        return entity.getReqId();
    }

    public String getDescription() {
        return entity.getDescription();
    }

    public LocalDateTime getCreatedTime() {
        return entity.getCreatedTime();
    }

    public LocalDateTime getLastUpdatedTime() {
        return entity.getUpdatedTime();
    }

    public ReportStatus getPdfReportStatus() {
        return entity.getPdfReport().getStatus();
    }

    public ReportStatus getExcelReportStatus() {
        return entity.getExcelReport().getStatus();
    }

    public LocalDateTime getPdfReportUpdatedTime() {
        return entity.getPdfReport().getUpdatedTime();
    }

    public LocalDateTime getExcelReportUpdatedTime() {
        return entity.getExcelReport().getUpdatedTime();
    }
}
