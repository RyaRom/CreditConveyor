package com.conveyor.model.DTO;

import com.conveyor.model.EmploymentStatus;
import com.conveyor.model.Position;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class EmploymentDTO {
    @NotNull
    private EmploymentStatus employmentStatus;
    @Pattern(regexp = "[0-9]{12}")
    private String employmentINN;
    private Double salary;
    private Position position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
