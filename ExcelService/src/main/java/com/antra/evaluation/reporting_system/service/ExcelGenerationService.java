package com.antra.evaluation.reporting_system.service;

import com.antra.evaluation.reporting_system.pojo.report.ExcelData;

import java.io.File;
import java.io.IOException;

public interface ExcelGenerationService {
    File generateExcelReport(ExcelData data) throws IOException;
}
