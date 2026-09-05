package com.nerlogistics.backend.repository;

import com.nerlogistics.backend.entity.RoadReport;
import com.nerlogistics.backend.enums.ReportStatus;
import com.nerlogistics.backend.enums.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoadReportRepository extends JpaRepository<RoadReport, Long> {
    List<RoadReport> findByStatus(ReportStatus status);
    List<RoadReport> findByType(ReportType type);
    List<RoadReport> findAllByOrderByCreatedAtDesc();
}
