package com.antra.evaluation.reporting_system.repo;

import com.antra.evaluation.reporting_system.pojo.report.ExcelFile;

import java.util.List;
import java.util.Optional;

public interface ExcelRepository {
    Optional<ExcelFile> getFileById(String id);

    ExcelFile saveFile(ExcelFile file);

    ExcelFile deleteFile(String id);

    List<ExcelFile> getFiles();
}
