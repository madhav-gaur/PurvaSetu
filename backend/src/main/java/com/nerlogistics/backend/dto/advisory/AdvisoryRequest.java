package com.nerlogistics.backend.dto.advisory;

import com.nerlogistics.backend.enums.AdvisoryType;
import com.nerlogistics.backend.enums.Severity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvisoryRequest {
    private Long roadSegmentId;

    @NotNull(message = "Advisory type is required")
    private AdvisoryType type;

    private Severity severity;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private String source;
    private String sourceUrl;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
    private String status;
}
