package com.gateway.model.json;

import com.gateway.model.enums.EmploymentStatus;
import com.gateway.model.enums.Position;

import java.math.BigDecimal;

public record Employment(
        EmploymentStatus status,
        String employmentInn,
        BigDecimal salary,
        Position position,
        Integer workExperienceTotal,
        Integer workExperienceCurrent
) {
}
