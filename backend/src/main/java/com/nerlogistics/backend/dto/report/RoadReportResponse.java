package com.nerlogistics.backend.dto.report;

import com.nerlogistics.backend.enums.ReportStatus;
import com.nerlogistics.backend.enums.ReportType;
import com.nerlogistics.backend.enums.Severity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoadReportResponse {
    private Long id;
    private String reporter;
    private Double latitude;
    private Double longitude;
    private ReportType type;
    private String description;
    private Severity severity;
    private ReportStatus status;
    private String photoUrl;
    private LocalDateTime createdAt;
    private LocalDateTime verifiedAt;
}
