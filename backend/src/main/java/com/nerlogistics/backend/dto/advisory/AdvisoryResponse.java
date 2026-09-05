package com.nerlogistics.backend.dto.advisory;

import com.nerlogistics.backend.enums.AdvisoryType;
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
public class AdvisoryResponse {
    private Long id;
    private Long roadSegmentId;
    private String roadName;
    private AdvisoryType type;
    private Severity severity;
    private String title;
    private String description;
    private String source;
    private String sourceUrl;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
    private String status;
    private LocalDateTime createdAt;
}
