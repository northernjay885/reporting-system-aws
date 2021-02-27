package com.antra.evaluation.reporting_system.repo;

import com.antra.evaluation.reporting_system.pojo.report.PDFFile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PDFRepository extends MongoRepository<PDFFile, String> {
}
