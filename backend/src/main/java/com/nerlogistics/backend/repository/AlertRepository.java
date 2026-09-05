package com.nerlogistics.backend.repository;

import com.nerlogistics.backend.entity.Alert;
import com.nerlogistics.backend.enums.Severity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByAcknowledgedFalseOrderByCreatedAtDesc();
    List<Alert> findAllByOrderByCreatedAtDesc();
    List<Alert> findBySeverityAndAcknowledgedFalse(Severity severity);
    List<Alert> findByVehicleIdOrderByCreatedAtDesc(Long vehicleId);
    List<Alert> findByShipmentIdOrderByCreatedAtDesc(Long shipmentId);
}
