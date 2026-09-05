package com.nerlogistics.backend.repository;

import com.nerlogistics.backend.entity.RiskPrediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RiskPredictionRepository extends JpaRepository<RiskPrediction, Long> {
    Optional<RiskPrediction> findTopByLatitudeAndLongitudeOrderByPredictedAtDesc(Double latitude, Double longitude);
    List<RiskPrediction> findTop20ByOrderByPredictedAtDesc();
}
