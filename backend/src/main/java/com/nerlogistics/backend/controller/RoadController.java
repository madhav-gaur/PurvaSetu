package com.nerlogistics.backend.controller;

import com.nerlogistics.backend.entity.Road;
import com.nerlogistics.backend.entity.RoadSegment;
import com.nerlogistics.backend.service.RoadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roads")
@Tag(name = "Roads & Corridors", description = "Highways, passes, and road segment accessibility")
@RequiredArgsConstructor
public class RoadController {

    private final RoadService roadService;

    @GetMapping
    @Operation(summary = "Get all highway corridors in North East India")
    public ResponseEntity<List<Road>> getAllRoads() {
        return ResponseEntity.ok(roadService.getAllRoads());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get road details by ID")
    public ResponseEntity<Road> getRoadById(@PathVariable Long id) {
        return ResponseEntity.ok(roadService.getRoadById(id));
    }

    @PostMapping
    @Operation(summary = "Register a new road or highway corridor")
    public ResponseEntity<Road> createRoad(@RequestBody Road road) {
        return ResponseEntity.ok(roadService.createRoad(road));
    }

    @GetMapping("/segments")
    @Operation(summary = "Get all road segments across the network")
    public ResponseEntity<List<RoadSegment>> getAllSegments() {
        return ResponseEntity.ok(roadService.getAllSegments());
    }

    @GetMapping("/{id}/segments")
    @Operation(summary = "Get all segments belonging to a specific highway corridor")
    public ResponseEntity<List<RoadSegment>> getSegmentsByRoadId(@PathVariable Long id) {
        return ResponseEntity.ok(roadService.getSegmentsByRoadId(id));
    }
}
