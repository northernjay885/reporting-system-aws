package com.antra.ClientServiceSync.repository;

import com.antra.ClientServiceSync.entity.ReportRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRequestRepo extends JpaRepository<ReportRequestEntity, String> {
}
