package com.nerlogistics.backend.repository;

import com.nerlogistics.backend.entity.RoadSegment;
import com.nerlogistics.backend.enums.RoadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoadSegmentRepository extends JpaRepository<RoadSegment, Long> {
    List<RoadSegment> findByRoadId(Long roadId);
    List<RoadSegment> findByStatus(RoadStatus status);
}
