package com.nerlogistics.backend.controller;

import com.nerlogistics.backend.entity.HazardEvent;
import com.nerlogistics.backend.service.HazardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hazards")
@Tag(name = "Disaster Hazards & Events", description = "Active natural disaster hazards, landslides, flood alerts, and road obstructions")
@RequiredArgsConstructor
public class HazardController {

    private final HazardService hazardService;

    @GetMapping
    @Operation(summary = "Get all disaster hazards")
    public ResponseEntity<List<HazardEvent>> getAllHazards() {
        return ResponseEntity.ok(hazardService.getAllHazards());
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active disaster hazards")
    public ResponseEntity<List<HazardEvent>> getActiveHazards() {
        return ResponseEntity.ok(hazardService.getAllActiveHazards());
    }

    @PostMapping
    @Operation(summary = "Register or report a new hazard event")
    public ResponseEntity<HazardEvent> createHazard(@RequestBody HazardEvent hazard) {
        return ResponseEntity.ok(hazardService.createHazard(hazard));
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate an existing hazard event")
    public ResponseEntity<Void> deactivateHazard(@PathVariable Long id) {
        hazardService.deactivateHazard(id);
        return ResponseEntity.noContent().build();
    }
}
