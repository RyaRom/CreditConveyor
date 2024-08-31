package com.dossier.model.json;

import com.dossier.model.enums.EmploymentStatus;
import com.dossier.model.enums.Position;

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
