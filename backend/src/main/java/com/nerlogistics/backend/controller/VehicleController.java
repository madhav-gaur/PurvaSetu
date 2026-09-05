package com.nerlogistics.backend.controller;

import com.nerlogistics.backend.dto.vehicle.LocationUpdateRequest;
import com.nerlogistics.backend.dto.vehicle.VehicleRequest;
import com.nerlogistics.backend.dto.vehicle.VehicleResponse;
import com.nerlogistics.backend.entity.VehicleLocation;
import com.nerlogistics.backend.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@Tag(name = "Vehicles & Telemetry", description = "Vehicle fleet tracking, GPS updates, and history")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    @Operation(summary = "Get all vehicles with live status")
    public ResponseEntity<List<VehicleResponse>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vehicle by ID")
    public ResponseEntity<VehicleResponse> getVehicleById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }

    @PostMapping
    @Operation(summary = "Register a new vehicle into the fleet")
    public ResponseEntity<VehicleResponse> createVehicle(@Valid @RequestBody VehicleRequest request) {
        return ResponseEntity.ok(vehicleService.createVehicle(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update vehicle details or operational status")
    public ResponseEntity<VehicleResponse> updateVehicle(@PathVariable Long id, @Valid @RequestBody VehicleRequest request) {
        return ResponseEntity.ok(vehicleService.updateVehicle(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove vehicle from fleet")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/location")
    @Operation(summary = "Receive GPS telemetry update from vehicle or Traccar simulator")
    public ResponseEntity<VehicleResponse> updateLocation(@PathVariable Long id, @Valid @RequestBody LocationUpdateRequest request) {
        return ResponseEntity.ok(vehicleService.updateLocation(id, request));
    }

    @GetMapping("/{id}/locations")
    @Operation(summary = "Get historical GPS breadcrumbs for vehicle movement playback")
    public ResponseEntity<List<VehicleLocation>> getLocationHistory(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getLocationHistory(id));
    }
}
