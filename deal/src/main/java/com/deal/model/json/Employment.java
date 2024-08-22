package com.deal.model.json;

import com.deal.model.enums.EmploymentStatus;
import com.deal.model.enums.Position;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Employment {
    private EmploymentStatus status;
    private String employmentInn;
    private BigDecimal salary;
    private Position position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
