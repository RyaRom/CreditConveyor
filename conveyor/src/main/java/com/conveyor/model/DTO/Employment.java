package com.conveyor.model.DTO;

import com.conveyor.model.enums.EmploymentStatus;
import com.conveyor.model.enums.Position;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
public class Employment {
    @NotNull
    private EmploymentStatus employmentStatus;
    @Pattern(regexp = "[0-9]{12}")
    private String employmentINN;
    private Double salary;
    private Position position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
