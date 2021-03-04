package com.antra.evaluation.reporting_system.pojo.request;

import javax.validation.constraints.NotNull;

public class MultiSheetExcelRequest extends ExcelRequest{
    @NotNull
    private String splitBy;

    public String getSplitBy() {
        return splitBy;
    }

    public void setSplitBy(String splitBy) {
        this.splitBy = splitBy;
    }
}
