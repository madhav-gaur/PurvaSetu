package com.nerlogistics.backend.repository;

import com.nerlogistics.backend.entity.WeatherObservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherObservationRepository extends JpaRepository<WeatherObservation, Long> {
    Optional<WeatherObservation> findTopByLatitudeAndLongitudeOrderByObservedAtDesc(Double latitude, Double longitude);
    List<WeatherObservation> findByObservedAtAfter(LocalDateTime since);
    List<WeatherObservation> findTop50ByOrderByObservedAtDesc();
}
