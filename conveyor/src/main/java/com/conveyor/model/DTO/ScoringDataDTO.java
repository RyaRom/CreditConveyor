package com.conveyor.model.DTO;

import com.conveyor.model.enums.Gender;
import com.conveyor.model.enums.MaritalStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ScoringDataDTO {
    @Min(10000)
    private Double amount;

    @Min(6)
    private Integer term;

    @Pattern(regexp = "[A-Za-z\\-]{2,30}")
    private String firstName;

    @Pattern(regexp = "[A-Za-z\\-]{2,30}")
    private String lastName;

    @Pattern(regexp = "[A-Za-z\\-]{2,30}")
    private String middleName;

    @Pattern(regexp = "[0-9]{4}")
    private String passportSeries;

    @Pattern(regexp = "[0-9]{4}")
    private String passportNumber;

    @Pattern(regexp = "[0-9]{20}")
    private String account;

    private Gender gender;
    private LocalDate birthdate;
    private LocalDate passportIssueDate;
    private LocalDate passportIssueBranch;
    private MaritalStatus maritalStatus;
    private Integer dependentAmount;
    private Employment employment;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
}
