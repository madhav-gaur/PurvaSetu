package com.nerlogistics.backend.repository;

import com.nerlogistics.backend.entity.RoadAdvisory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoadAdvisoryRepository extends JpaRepository<RoadAdvisory, Long> {
    List<RoadAdvisory> findByStatus(String status);
    List<RoadAdvisory> findAllByOrderByCreatedAtDesc();
}
