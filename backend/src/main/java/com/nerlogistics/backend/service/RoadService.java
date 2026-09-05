package com.nerlogistics.backend.service;

import com.nerlogistics.backend.entity.Road;
import com.nerlogistics.backend.entity.RoadSegment;
import com.nerlogistics.backend.enums.RoadStatus;
import com.nerlogistics.backend.exception.ResourceNotFoundException;
import com.nerlogistics.backend.repository.RoadRepository;
import com.nerlogistics.backend.repository.RoadSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoadService {

    private final RoadRepository roadRepository;
    private final RoadSegmentRepository roadSegmentRepository;

    public List<Road> getAllRoads() {
        return roadRepository.findAll();
    }

    public Road getRoadById(Long id) {
        return roadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Road not found with id: " + id));
    }

    @Transactional
    public Road createRoad(Road road) {
        return roadRepository.save(road);
    }

    public List<RoadSegment> getAllSegments() {
        return roadSegmentRepository.findAll();
    }

    public List<RoadSegment> getSegmentsByRoadId(Long roadId) {
        return roadSegmentRepository.findByRoadId(roadId);
    }

    @Transactional
    public RoadSegment updateSegmentStatus(Long segmentId, RoadStatus status, Double riskScore) {
        RoadSegment segment = roadSegmentRepository.findById(segmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Road segment not found: " + segmentId));

        segment.setStatus(status);
        if (riskScore != null) {
            segment.setRiskScore(riskScore);
            segment.setAccessibilityScore(Math.max(0.0, 1.0 - riskScore));
        }
        return roadSegmentRepository.save(segment);
    }
}
