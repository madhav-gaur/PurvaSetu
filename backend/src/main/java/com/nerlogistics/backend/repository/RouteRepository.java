package com.nerlogistics.backend.repository;

import com.nerlogistics.backend.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findByShipmentId(Long shipmentId);
    List<Route> findByVehicleId(Long vehicleId);
    List<Route> findByStatus(String status);
}
