package com.nerlogistics.backend.service;

import com.nerlogistics.backend.dto.report.RoadReportRequest;
import com.nerlogistics.backend.dto.report.RoadReportResponse;
import com.nerlogistics.backend.entity.Alert;
import com.nerlogistics.backend.entity.RoadReport;
import com.nerlogistics.backend.enums.AlertType;
import com.nerlogistics.backend.enums.ReportStatus;
import com.nerlogistics.backend.enums.Severity;
import com.nerlogistics.backend.exception.ResourceNotFoundException;
import com.nerlogistics.backend.repository.AlertRepository;
import com.nerlogistics.backend.repository.RoadReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoadReportService {

    private final RoadReportRepository roadReportRepository;
    private final AlertRepository alertRepository;

    public List<RoadReportResponse> getAllReports() {
        return roadReportRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public RoadReportResponse getReportById(Long id) {
        RoadReport report = roadReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Road report not found with id: " + id));
        return mapToResponse(report);
    }

    @Transactional
    public RoadReportResponse createReport(RoadReportRequest request) {
        RoadReport report = RoadReport.builder()
                .reporter(request.getReporter())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .type(request.getType())
                .description(request.getDescription())
                .severity(request.getSeverity() != null ? request.getSeverity() : Severity.MEDIUM)
                .status(request.getStatus() != null ? request.getStatus() : ReportStatus.PENDING)
                .photoUrl(request.getPhotoUrl())
                .createdAt(LocalDateTime.now())
                .build();

        report = roadReportRepository.save(report);

        // Generate Alert if report severity is HIGH or CRITICAL
        if (report.getSeverity() == Severity.HIGH || report.getSeverity() == Severity.CRITICAL) {
            AlertType alertType = switch (report.getType()) {
                case FLOOD -> AlertType.FLOOD_WARNING;
                case LANDSLIDE -> AlertType.LANDSLIDE_WARNING;
                case ROAD_BLOCKED, BRIDGE_DAMAGE -> AlertType.ROAD_CLOSURE;
                default -> AlertType.HIGH_RISK_ROUTE;
            };

            Alert alert = Alert.builder()
                    .type(alertType)
                    .severity(report.getSeverity())
                    .title("Field Incident: " + report.getType() + " Reported")
                    .message(report.getDescription() != null ? report.getDescription() : "Hazard incident reported near (" + report.getLatitude() + ", " + report.getLongitude() + ")")
                    .latitude(report.getLatitude())
                    .longitude(report.getLongitude())
                    .acknowledged(false)
                    .createdAt(LocalDateTime.now())
                    .build();
            alertRepository.save(alert);
        }

        return mapToResponse(report);
    }

    @Transactional
    public RoadReportResponse updateReport(Long id, RoadReportRequest request) {
        RoadReport report = roadReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Road report not found with id: " + id));

        report.setLatitude(request.getLatitude());
        report.setLongitude(request.getLongitude());
        if (request.getType() != null) report.setType(request.getType());
        if (request.getDescription() != null) report.setDescription(request.getDescription());
        if (request.getSeverity() != null) report.setSeverity(request.getSeverity());
        if (request.getStatus() != null) report.setStatus(request.getStatus());
        if (request.getPhotoUrl() != null) report.setPhotoUrl(request.getPhotoUrl());

        return mapToResponse(roadReportRepository.save(report));
    }

    @Transactional
    public RoadReportResponse verifyReport(Long id) {
        RoadReport report = roadReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Road report not found with id: " + id));

        report.setStatus(ReportStatus.VERIFIED);
        report.setVerifiedAt(LocalDateTime.now());
        return mapToResponse(roadReportRepository.save(report));
    }

    @Transactional
    public RoadReportResponse resolveReport(Long id) {
        RoadReport report = roadReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Road report not found with id: " + id));

        report.setStatus(ReportStatus.RESOLVED);
        return mapToResponse(roadReportRepository.save(report));
    }

    @Transactional
    public void deleteReport(Long id) {
        RoadReport report = roadReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Road report not found with id: " + id));
        roadReportRepository.delete(report);
    }

    public RoadReportResponse mapToResponse(RoadReport report) {
        if (report == null) return null;
        return RoadReportResponse.builder()
                .id(report.getId())
                .reporter(report.getReporter())
                .latitude(report.getLatitude())
                .longitude(report.getLongitude())
                .type(report.getType())
                .description(report.getDescription())
                .severity(report.getSeverity())
                .status(report.getStatus())
                .photoUrl(report.getPhotoUrl())
                .createdAt(report.getCreatedAt())
                .verifiedAt(report.getVerifiedAt())
                .build();
    }
}
