package com.nerlogistics.backend.service;

import com.nerlogistics.backend.dto.advisory.AdvisoryRequest;
import com.nerlogistics.backend.dto.advisory.AdvisoryResponse;
import com.nerlogistics.backend.entity.Alert;
import com.nerlogistics.backend.entity.RoadAdvisory;
import com.nerlogistics.backend.entity.RoadSegment;
import com.nerlogistics.backend.enums.AlertType;
import com.nerlogistics.backend.enums.Severity;
import com.nerlogistics.backend.exception.ResourceNotFoundException;
import com.nerlogistics.backend.repository.AlertRepository;
import com.nerlogistics.backend.repository.RoadAdvisoryRepository;
import com.nerlogistics.backend.repository.RoadSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdvisoryService {

    private final RoadAdvisoryRepository roadAdvisoryRepository;
    private final RoadSegmentRepository roadSegmentRepository;
    private final AlertRepository alertRepository;

    public List<AdvisoryResponse> getAllAdvisories() {
        return roadAdvisoryRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public AdvisoryResponse getAdvisoryById(Long id) {
        RoadAdvisory advisory = roadAdvisoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Road advisory not found with id: " + id));
        return mapToResponse(advisory);
    }

    @Transactional
    public AdvisoryResponse createAdvisory(AdvisoryRequest request) {
        RoadSegment segment = null;
        if (request.getRoadSegmentId() != null) {
            segment = roadSegmentRepository.findById(request.getRoadSegmentId()).orElse(null);
        }

        RoadAdvisory advisory = RoadAdvisory.builder()
                .roadSegment(segment)
                .type(request.getType())
                .severity(request.getSeverity() != null ? request.getSeverity() : Severity.HIGH)
                .title(request.getTitle())
                .description(request.getDescription())
                .source(request.getSource() != null ? request.getSource() : "Official Administration Notification")
                .sourceUrl(request.getSourceUrl())
                .validFrom(request.getValidFrom() != null ? request.getValidFrom() : LocalDateTime.now())
                .validUntil(request.getValidUntil() != null ? request.getValidUntil() : LocalDateTime.now().plusDays(2))
                .status(request.getStatus() != null ? request.getStatus() : "ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();

        advisory = roadAdvisoryRepository.save(advisory);

        // Generate Alert for Road Closure or Curfew
        Alert alert = Alert.builder()
                .type(AlertType.ROAD_CLOSURE)
                .severity(advisory.getSeverity())
                .title("Official Advisory: " + advisory.getTitle())
                .message(advisory.getDescription())
                .acknowledged(false)
                .createdAt(LocalDateTime.now())
                .build();
        alertRepository.save(alert);

        return mapToResponse(advisory);
    }

    @Transactional
    public AdvisoryResponse updateAdvisory(Long id, AdvisoryRequest request) {
        RoadAdvisory advisory = roadAdvisoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Road advisory not found with id: " + id));

        advisory.setTitle(request.getTitle());
        advisory.setDescription(request.getDescription());
        if (request.getType() != null) advisory.setType(request.getType());
        if (request.getSeverity() != null) advisory.setSeverity(request.getSeverity());
        if (request.getSource() != null) advisory.setSource(request.getSource());
        if (request.getSourceUrl() != null) advisory.setSourceUrl(request.getSourceUrl());
        if (request.getValidFrom() != null) advisory.setValidFrom(request.getValidFrom());
        if (request.getValidUntil() != null) advisory.setValidUntil(request.getValidUntil());
        if (request.getStatus() != null) advisory.setStatus(request.getStatus());

        return mapToResponse(roadAdvisoryRepository.save(advisory));
    }

    @Transactional
    public void deleteAdvisory(Long id) {
        RoadAdvisory advisory = roadAdvisoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Road advisory not found with id: " + id));
        roadAdvisoryRepository.delete(advisory);
    }

    public AdvisoryResponse mapToResponse(RoadAdvisory advisory) {
        if (advisory == null) return null;
        return AdvisoryResponse.builder()
                .id(advisory.getId())
                .roadSegmentId(advisory.getRoadSegment() != null ? advisory.getRoadSegment().getId() : null)
                .roadName(advisory.getRoadSegment() != null && advisory.getRoadSegment().getRoad() != null
                        ? advisory.getRoadSegment().getRoad().getName() : "Regional Highway Network")
                .type(advisory.getType())
                .severity(advisory.getSeverity())
                .title(advisory.getTitle())
                .description(advisory.getDescription())
                .source(advisory.getSource())
                .sourceUrl(advisory.getSourceUrl())
                .validFrom(advisory.getValidFrom())
                .validUntil(advisory.getValidUntil())
                .status(advisory.getStatus())
                .createdAt(advisory.getCreatedAt())
                .build();
    }
}
