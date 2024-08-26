package com.deal.model.json;

import com.deal.model.enums.EmploymentStatus;
import com.deal.model.enums.Position;

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
