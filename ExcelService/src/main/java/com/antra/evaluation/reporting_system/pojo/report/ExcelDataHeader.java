package com.antra.evaluation.reporting_system.pojo.report;

public class ExcelDataHeader {
    private String name;
    private ExcelDataType type;
    private int width;

    public ExcelDataHeader() {
    }

    public ExcelDataHeader(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExcelDataType getType() {
        return type;
    }

    public void setType(ExcelDataType type) {
        this.type = type;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
