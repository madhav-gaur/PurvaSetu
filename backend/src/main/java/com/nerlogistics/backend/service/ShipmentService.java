package com.nerlogistics.backend.service;

import com.nerlogistics.backend.dto.shipment.ShipmentRequest;
import com.nerlogistics.backend.dto.shipment.ShipmentResponse;
import com.nerlogistics.backend.entity.Shipment;
import com.nerlogistics.backend.entity.Vehicle;
import com.nerlogistics.backend.enums.ShipmentPriority;
import com.nerlogistics.backend.enums.ShipmentStatus;
import com.nerlogistics.backend.enums.VehicleStatus;
import com.nerlogistics.backend.exception.BadRequestException;
import com.nerlogistics.backend.exception.ResourceNotFoundException;
import com.nerlogistics.backend.repository.ShipmentRepository;
import com.nerlogistics.backend.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final VehicleRepository vehicleRepository;
    private final VehicleService vehicleService;

    public List<ShipmentResponse> getAllShipments() {
        return shipmentRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ShipmentResponse getShipmentById(Long id) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + id));
        return mapToResponse(shipment);
    }

    @Transactional
    public ShipmentResponse createShipment(ShipmentRequest request) {
        String trackingNumber = request.getTrackingNumber();
        if (trackingNumber == null || trackingNumber.isBlank()) {
            trackingNumber = "NER-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } else if (shipmentRepository.findByTrackingNumber(trackingNumber).isPresent()) {
            throw new BadRequestException("Shipment tracking number already exists: " + trackingNumber);
        }

        Vehicle vehicle = null;
        if (request.getVehicleId() != null) {
            vehicle = vehicleRepository.findById(request.getVehicleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + request.getVehicleId()));
        }

        Shipment shipment = Shipment.builder()
                .trackingNumber(trackingNumber)
                .source(request.getSource())
                .destination(request.getDestination())
                .priority(request.getPriority() != null ? request.getPriority() : ShipmentPriority.MEDIUM)
                .status(request.getStatus() != null ? request.getStatus() : (vehicle != null ? ShipmentStatus.ASSIGNED : ShipmentStatus.CREATED))
                .cargoType(request.getCargoType() != null ? request.getCargoType() : "General Cargo")
                .weight(request.getWeight() != null ? request.getWeight() : 5.0)
                .vehicle(vehicle)
                .expectedDelivery(request.getExpectedDelivery() != null ? request.getExpectedDelivery() : LocalDateTime.now().plusHours(8))
                .build();

        shipment = shipmentRepository.save(shipment);

        if (vehicle != null) {
            vehicle.setCurrentShipment(shipment.getTrackingNumber());
            vehicle.setStatus(VehicleStatus.IN_TRANSIT);
            vehicleRepository.save(vehicle);
        }

        return mapToResponse(shipment);
    }

    @Transactional
    public ShipmentResponse updateShipment(Long id, ShipmentRequest request) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + id));

        shipment.setSource(request.getSource());
        shipment.setDestination(request.getDestination());
        if (request.getPriority() != null) shipment.setPriority(request.getPriority());
        if (request.getStatus() != null) shipment.setStatus(request.getStatus());
        if (request.getCargoType() != null) shipment.setCargoType(request.getCargoType());
        if (request.getWeight() != null) shipment.setWeight(request.getWeight());
        if (request.getExpectedDelivery() != null) shipment.setExpectedDelivery(request.getExpectedDelivery());

        if (request.getVehicleId() != null) {
            Vehicle v = vehicleRepository.findById(request.getVehicleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + request.getVehicleId()));
            shipment.setVehicle(v);
        }

        return mapToResponse(shipmentRepository.save(shipment));
    }

    @Transactional
    public void deleteShipment(Long id) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + id));
        shipmentRepository.delete(shipment);
    }

    @Transactional
    public ShipmentResponse assignVehicle(Long shipmentId, Long vehicleId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + shipmentId));

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + vehicleId));

        shipment.setVehicle(vehicle);
        shipment.setStatus(ShipmentStatus.ASSIGNED);
        shipment = shipmentRepository.save(shipment);

        vehicle.setCurrentShipment(shipment.getTrackingNumber());
        vehicle.setStatus(VehicleStatus.IN_TRANSIT);
        vehicleRepository.save(vehicle);

        return mapToResponse(shipment);
    }

    @Transactional
    public ShipmentResponse updateStatus(Long shipmentId, ShipmentStatus status) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + shipmentId));

        shipment.setStatus(status);
        if (status == ShipmentStatus.DELIVERED) {
            shipment.setActualDelivery(LocalDateTime.now());
            if (shipment.getVehicle() != null) {
                Vehicle v = shipment.getVehicle();
                v.setStatus(VehicleStatus.AVAILABLE);
                v.setCurrentShipment(null);
                vehicleRepository.save(v);
            }
        }
        return mapToResponse(shipmentRepository.save(shipment));
    }

    public ShipmentResponse mapToResponse(Shipment shipment) {
        if (shipment == null) return null;
        return ShipmentResponse.builder()
                .id(shipment.getId())
                .trackingNumber(shipment.getTrackingNumber())
                .source(shipment.getSource())
                .destination(shipment.getDestination())
                .priority(shipment.getPriority())
                .status(shipment.getStatus())
                .cargoType(shipment.getCargoType())
                .weight(shipment.getWeight())
                .vehicle(vehicleService.mapToResponse(shipment.getVehicle()))
                .createdAt(shipment.getCreatedAt())
                .expectedDelivery(shipment.getExpectedDelivery())
                .actualDelivery(shipment.getActualDelivery())
                .build();
    }
}
