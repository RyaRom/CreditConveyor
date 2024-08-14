package com.deal.model.json;

import com.deal.model.enums.EmploymentStatus;
import com.deal.model.enums.Position;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Employment {
    private EmploymentStatus status;
    private String employment_inn;
    private Double salary;
    private Position position;
    private Integer work_experience_total;
    private Integer work_experience_current;
}
