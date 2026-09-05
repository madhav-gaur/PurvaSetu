package com.nerlogistics.backend.controller;

import com.nerlogistics.backend.dto.shipment.AssignVehicleRequest;
import com.nerlogistics.backend.dto.shipment.ShipmentRequest;
import com.nerlogistics.backend.dto.shipment.ShipmentResponse;
import com.nerlogistics.backend.dto.shipment.UpdateStatusRequest;
import com.nerlogistics.backend.service.ShipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipments")
@Tag(name = "Shipments & Consignments", description = "Shipment tracking, priority management, and vehicle allocation")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    @GetMapping
    @Operation(summary = "Get all shipments")
    public ResponseEntity<List<ShipmentResponse>> getAllShipments() {
        return ResponseEntity.ok(shipmentService.getAllShipments());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get shipment by ID")
    public ResponseEntity<ShipmentResponse> getShipmentById(@PathVariable Long id) {
        return ResponseEntity.ok(shipmentService.getShipmentById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new shipment (Priority: CRITICAL, HIGH, MEDIUM, LOW)")
    public ResponseEntity<ShipmentResponse> createShipment(@Valid @RequestBody ShipmentRequest request) {
        return ResponseEntity.ok(shipmentService.createShipment(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update shipment details")
    public ResponseEntity<ShipmentResponse> updateShipment(@PathVariable Long id, @Valid @RequestBody ShipmentRequest request) {
        return ResponseEntity.ok(shipmentService.updateShipment(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete shipment")
    public ResponseEntity<Void> deleteShipment(@PathVariable Long id) {
        shipmentService.deleteShipment(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/assign-vehicle")
    @Operation(summary = "Assign a fleet vehicle to a shipment")
    public ResponseEntity<ShipmentResponse> assignVehicle(@PathVariable Long id, @Valid @RequestBody AssignVehicleRequest request) {
        return ResponseEntity.ok(shipmentService.assignVehicle(id, request.getVehicleId()));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update shipment lifecycle status")
    public ResponseEntity<ShipmentResponse> updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateStatusRequest request) {
        return ResponseEntity.ok(shipmentService.updateStatus(id, request.getStatus()));
    }
}
