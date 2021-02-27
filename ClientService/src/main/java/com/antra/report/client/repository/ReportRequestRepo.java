package com.antra.report.client.repository;

import com.antra.report.client.entity.ReportRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRequestRepo extends JpaRepository<ReportRequestEntity, String> {
}
