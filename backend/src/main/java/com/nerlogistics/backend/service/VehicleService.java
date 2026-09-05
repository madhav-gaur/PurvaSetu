package com.nerlogistics.backend.service;

import com.nerlogistics.backend.dto.vehicle.LocationUpdateRequest;
import com.nerlogistics.backend.dto.vehicle.VehicleRequest;
import com.nerlogistics.backend.dto.vehicle.VehicleResponse;
import com.nerlogistics.backend.entity.Vehicle;
import com.nerlogistics.backend.entity.VehicleLocation;
import com.nerlogistics.backend.enums.VehicleStatus;
import com.nerlogistics.backend.exception.BadRequestException;
import com.nerlogistics.backend.exception.ResourceNotFoundException;
import com.nerlogistics.backend.repository.VehicleLocationRepository;
import com.nerlogistics.backend.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleLocationRepository vehicleLocationRepository;

    public List<VehicleResponse> getAllVehicles() {
        return vehicleRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public VehicleResponse getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
        return mapToResponse(vehicle);
    }

    @Transactional
    public VehicleResponse createVehicle(VehicleRequest request) {
        if (vehicleRepository.findByVehicleNumber(request.getVehicleNumber()).isPresent()) {
            throw new BadRequestException("Vehicle number already registered: " + request.getVehicleNumber());
        }

        Vehicle vehicle = Vehicle.builder()
                .vehicleNumber(request.getVehicleNumber())
                .vehicleType(request.getVehicleType() != null ? request.getVehicleType() : "Standard Heavy Truck")
                .driver(request.getDriver())
                .driverPhone(request.getDriverPhone())
                .currentLatitude(request.getCurrentLatitude())
                .currentLongitude(request.getCurrentLongitude())
                .speed(request.getSpeed() != null ? request.getSpeed() : 0.0)
                .status(request.getStatus() != null ? request.getStatus() : VehicleStatus.AVAILABLE)
                .currentShipment(request.getCurrentShipment())
                .lastUpdated(LocalDateTime.now())
                .build();

        vehicle = vehicleRepository.save(vehicle);

        // Record initial location breadcrumb
        VehicleLocation loc = VehicleLocation.builder()
                .vehicleId(vehicle.getId())
                .latitude(vehicle.getCurrentLatitude())
                .longitude(vehicle.getCurrentLongitude())
                .speed(vehicle.getSpeed())
                .timestamp(LocalDateTime.now())
                .build();
        vehicleLocationRepository.save(loc);

        return mapToResponse(vehicle);
    }

    @Transactional
    public VehicleResponse updateVehicle(Long id, VehicleRequest request) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));

        vehicle.setVehicleType(request.getVehicleType());
        vehicle.setDriver(request.getDriver());
        vehicle.setDriverPhone(request.getDriverPhone());
        if (request.getStatus() != null) {
            vehicle.setStatus(request.getStatus());
        }
        if (request.getCurrentShipment() != null) {
            vehicle.setCurrentShipment(request.getCurrentShipment());
        }
        if (request.getCurrentLatitude() != null && request.getCurrentLongitude() != null) {
            vehicle.setCurrentLatitude(request.getCurrentLatitude());
            vehicle.setCurrentLongitude(request.getCurrentLongitude());
        }
        if (request.getSpeed() != null) {
            vehicle.setSpeed(request.getSpeed());
        }
        vehicle.setLastUpdated(LocalDateTime.now());

        return mapToResponse(vehicleRepository.save(vehicle));
    }

    @Transactional
    public void deleteVehicle(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
        vehicleRepository.delete(vehicle);
    }

    @Transactional
    public VehicleResponse updateLocation(Long id, LocationUpdateRequest request) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));

        vehicle.setCurrentLatitude(request.getLatitude());
        vehicle.setCurrentLongitude(request.getLongitude());
        if (request.getSpeed() != null) {
            vehicle.setSpeed(request.getSpeed());
        }
        vehicle.setLastUpdated(request.getTimestamp() != null ? request.getTimestamp() : LocalDateTime.now());
        vehicle = vehicleRepository.save(vehicle);

        VehicleLocation loc = VehicleLocation.builder()
                .vehicleId(vehicle.getId())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .speed(request.getSpeed() != null ? request.getSpeed() : 0.0)
                .timestamp(vehicle.getLastUpdated())
                .build();
        vehicleLocationRepository.save(loc);

        return mapToResponse(vehicle);
    }

    public List<VehicleLocation> getLocationHistory(Long id) {
        // Verify vehicle exists
        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicle not found with id: " + id);
        }
        return vehicleLocationRepository.findByVehicleIdOrderByTimestampDesc(id);
    }

    public VehicleResponse mapToResponse(Vehicle vehicle) {
        if (vehicle == null) return null;
        return VehicleResponse.builder()
                .id(vehicle.getId())
                .vehicleNumber(vehicle.getVehicleNumber())
                .vehicleType(vehicle.getVehicleType())
                .driver(vehicle.getDriver())
                .driverPhone(vehicle.getDriverPhone())
                .currentLatitude(vehicle.getCurrentLatitude())
                .currentLongitude(vehicle.getCurrentLongitude())
                .speed(vehicle.getSpeed())
                .status(vehicle.getStatus())
                .currentShipment(vehicle.getCurrentShipment())
                .lastUpdated(vehicle.getLastUpdated())
                .build();
    }
}
