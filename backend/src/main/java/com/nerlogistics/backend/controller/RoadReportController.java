package com.nerlogistics.backend.controller;

import com.nerlogistics.backend.dto.report.RoadReportRequest;
import com.nerlogistics.backend.dto.report.RoadReportResponse;
import com.nerlogistics.backend.service.RoadReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Field Road Reports", description = "Crowdsourced and Field Agent incident reporting (Flood, Landslide, Blockage)")
@RequiredArgsConstructor
public class RoadReportController {

    private final RoadReportService roadReportService;

    @GetMapping
    @Operation(summary = "Get all road incident reports")
    public ResponseEntity<List<RoadReportResponse>> getAllReports() {
        return ResponseEntity.ok(roadReportService.getAllReports());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get road report by ID")
    public ResponseEntity<RoadReportResponse> getReportById(@PathVariable Long id) {
        return ResponseEntity.ok(roadReportService.getReportById(id));
    }

    @PostMapping
    @Operation(summary = "Submit a new road incident report (from Field App / Driver)")
    public ResponseEntity<RoadReportResponse> createReport(@Valid @RequestBody RoadReportRequest request) {
        return ResponseEntity.ok(roadReportService.createReport(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update road report details")
    public ResponseEntity<RoadReportResponse> updateReport(@PathVariable Long id, @Valid @RequestBody RoadReportRequest request) {
        return ResponseEntity.ok(roadReportService.updateReport(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete road report")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        roadReportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/verify")
    @Operation(summary = "Verify a pending incident report (Operator / Admin action)")
    public ResponseEntity<RoadReportResponse> verifyReport(@PathVariable Long id) {
        return ResponseEntity.ok(roadReportService.verifyReport(id));
    }

    @PostMapping("/{id}/resolve")
    @Operation(summary = "Mark a road incident report as resolved")
    public ResponseEntity<RoadReportResponse> resolveReport(@PathVariable Long id) {
        return ResponseEntity.ok(roadReportService.resolveReport(id));
    }
}
