package com.nerlogistics.backend.repository;

import com.nerlogistics.backend.entity.VehicleLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VehicleLocationRepository extends JpaRepository<VehicleLocation, Long> {
    List<VehicleLocation> findByVehicleIdOrderByTimestampDesc(Long vehicleId);
    List<VehicleLocation> findByVehicleIdAndTimestampAfterOrderByTimestampAsc(Long vehicleId, LocalDateTime since);
}
