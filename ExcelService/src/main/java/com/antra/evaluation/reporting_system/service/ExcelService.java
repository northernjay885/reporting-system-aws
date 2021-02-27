package com.antra.evaluation.reporting_system.service;

import com.antra.evaluation.reporting_system.pojo.api.ExcelRequest;
import com.antra.evaluation.reporting_system.pojo.report.ExcelFile;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public interface ExcelService {
    InputStream getExcelBodyById(String id) throws FileNotFoundException;

    ExcelFile generateFile(ExcelRequest request, boolean multisheet);

    List<ExcelFile> getExcelList();

    ExcelFile deleteFile(String id) throws FileNotFoundException;
}
