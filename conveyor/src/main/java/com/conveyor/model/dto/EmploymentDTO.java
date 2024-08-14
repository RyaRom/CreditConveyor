package com.conveyor.model.dto;

import com.conveyor.model.enums.EmploymentStatus;
import com.conveyor.model.enums.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(title = "Employment", description = "Client's employment info.")
public class EmploymentDTO {

    @Schema(description = "Client's employment status.", example = "EMPLOYED")
    @NotNull
    private EmploymentStatus employmentStatus;

    @Schema(description = "Client's employer's INN.", example = "123456789012", pattern = "[0-9]{12}")
    @Pattern(regexp = "[0-9]{12}")
    private String employerINN;

    @Schema(description = "Client's salary.", example = "100000.00")
    private Double salary;

    @Schema(description = "Client's employment position.", example = "WORKER")
    private Position position;

    @Schema(description = "Client's total work experience.", example = "5")
    private Integer workExperienceTotal;

    @Schema(description = "Client's work experience in current job.", example = "2")
    private Integer workExperienceCurrent;

}