package com.nerlogistics.backend.repository;

import com.nerlogistics.backend.entity.Shipment;
import com.nerlogistics.backend.enums.ShipmentPriority;
import com.nerlogistics.backend.enums.ShipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    Optional<Shipment> findByTrackingNumber(String trackingNumber);
    List<Shipment> findByStatus(ShipmentStatus status);
    List<Shipment> findByPriority(ShipmentPriority priority);
    List<Shipment> findByVehicleId(Long vehicleId);
}
