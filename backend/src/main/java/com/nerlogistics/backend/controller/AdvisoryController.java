package com.nerlogistics.backend.controller;

import com.nerlogistics.backend.dto.advisory.AdvisoryRequest;
import com.nerlogistics.backend.dto.advisory.AdvisoryResponse;
import com.nerlogistics.backend.service.AdvisoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/advisories")
@Tag(name = "Official Advisories", description = "Verified government, traffic, and security road advisories")
@RequiredArgsConstructor
public class AdvisoryController {

    private final AdvisoryService advisoryService;

    @GetMapping
    @Operation(summary = "Get all official road & security advisories")
    public ResponseEntity<List<AdvisoryResponse>> getAllAdvisories() {
        return ResponseEntity.ok(advisoryService.getAllAdvisories());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get advisory by ID")
    public ResponseEntity<AdvisoryResponse> getAdvisoryById(@PathVariable Long id) {
        return ResponseEntity.ok(advisoryService.getAdvisoryById(id));
    }

    @PostMapping
    @Operation(summary = "Issue a new verified official advisory or road closure")
    public ResponseEntity<AdvisoryResponse> createAdvisory(@Valid @RequestBody AdvisoryRequest request) {
        return ResponseEntity.ok(advisoryService.createAdvisory(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update advisory details")
    public ResponseEntity<AdvisoryResponse> updateAdvisory(@PathVariable Long id, @Valid @RequestBody AdvisoryRequest request) {
        return ResponseEntity.ok(advisoryService.updateAdvisory(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete advisory")
    public ResponseEntity<Void> deleteAdvisory(@PathVariable Long id) {
        advisoryService.deleteAdvisory(id);
        return ResponseEntity.noContent().build();
    }
}
