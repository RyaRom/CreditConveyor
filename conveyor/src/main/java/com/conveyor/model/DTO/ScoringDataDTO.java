package com.conveyor.model.DTO;

import com.conveyor.model.enums.Gender;
import com.conveyor.model.enums.MaritalStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ScoringDataDTO {
    @NotNull
    @Min(10000)
    private Double amount;

    @NotNull
    @Min(6)
    private Integer term;

    @NotEmpty
    @Pattern(regexp = "[A-Za-z\\-]{2,30}")
    private String firstName;

    @NotEmpty
    @Pattern(regexp = "[A-Za-z\\-]{2,30}")
    private String lastName;

    @NotEmpty
    @Pattern(regexp = "[A-Za-z\\-]{2,30}")
    private String middleName;

    @NotEmpty
    @Pattern(regexp = "[0-9]{4}")
    private String passportSeries;

    @NotEmpty
    @Pattern(regexp = "[0-9]{4}")
    private String passportNumber;

    @NotEmpty
    @Pattern(regexp = "[0-9]{20}")
    private String account;

    @NotNull
    private Gender gender;
    @NotNull
    private LocalDate birthdate;
    @NotNull
    private LocalDate passportIssueDate;
    @NotNull
    private LocalDate passportIssueBranch;
    @NotNull
    private MaritalStatus maritalStatus;
    @NotNull
    private Integer dependentAmount;
    @NotNull
    private Employment employment;
    @NotNull
    private Boolean isInsuranceEnabled;
    @NotNull
    private Boolean isSalaryClient;
}
