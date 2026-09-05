package com.nerlogistics.backend.controller;

import com.nerlogistics.backend.dto.simulation.EmergencyDemoStatusDto;
import com.nerlogistics.backend.dto.simulation.SimulationTriggerRequest;
import com.nerlogistics.backend.service.SimulationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/simulation")
@Tag(name = "Simulation & SIH Demo Mode", description = "Disaster event injection, GPS movement simulation, and 14-Step Emergency Logistics Demo Flow")
@RequiredArgsConstructor
public class SimulationController {

    private final SimulationService simulationService;

    @PostMapping("/trigger")
    @Operation(summary = "Inject a simulated disaster event (HEAVY_RAIN, LANDSLIDE_BLOCKAGE, ROAD_CLOSURE)")
    public ResponseEntity<Map<String, String>> triggerScenario(@RequestBody SimulationTriggerRequest request) {
        simulationService.triggerScenario(request);
        return ResponseEntity.ok(Map.of("status", "SUCCESS", "message", "Simulation event triggered: " + request.getScenario()));
    }

    @GetMapping("/step/{stepNumber}")
    @Operation(summary = "Execute a specific step (1 to 14) of the SIH Emergency Logistics Demo")
    public ResponseEntity<EmergencyDemoStatusDto> runDemoStep(@PathVariable int stepNumber) {
        return ResponseEntity.ok(simulationService.runStep(stepNumber));
    }

    @PostMapping("/demo-emergency")
    @Operation(summary = "Advance or run the SIH Emergency Logistics Demo")
    public ResponseEntity<EmergencyDemoStatusDto> runEmergencyDemo(@RequestParam(required = false, defaultValue = "1") int step) {
        return ResponseEntity.ok(simulationService.runStep(step));
    }
}
