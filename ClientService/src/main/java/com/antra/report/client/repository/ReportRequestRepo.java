package com.antra.report.client.repository;

import com.antra.report.client.entity.ReportRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRequestRepo extends JpaRepository<ReportRequestEntity, String> {
}
