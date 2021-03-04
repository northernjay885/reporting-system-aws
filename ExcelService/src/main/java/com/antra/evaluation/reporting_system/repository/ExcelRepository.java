package com.antra.evaluation.reporting_system.repository;

import com.antra.evaluation.reporting_system.pojo.report.ExcelFile;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface ExcelRepository extends CrudRepository<ExcelFile, String> {

}
