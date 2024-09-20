package com.gateway.model.dto;


import com.gateway.model.enums.EmploymentStatus;
import com.gateway.model.enums.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@Schema(title = "Employment", description = "Client's employment info.")
public record EmploymentDTO(
        @Schema(description = "Client's employment status.", example = "EMPLOYED")
        @NotNull
        EmploymentStatus employmentStatus,

        @Schema(description = "Client's employer's INN.", example = "123456789012", pattern = "[0-9]{12}")
        @Pattern(regexp = "[0-9]{12}")
        String employerInn,

        @Schema(description = "Client's salary.", example = "100000.00")
        BigDecimal salary,

        @Schema(description = "Client's employment position.", example = "WORKER")
        Position position,

        @Schema(description = "Client's total work experience.", example = "5")
        Integer workExperienceTotal,

        @Schema(description = "Client's work experience in current job.", example = "2")
        Integer workExperienceCurrent
) {
}
