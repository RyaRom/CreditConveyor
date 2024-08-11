package com.conveyor.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanApplicationRequestDTO {
    @NotEmpty
    @Min(10000)
    private Double amount;

    @NotEmpty
    @Min(6)
    private Integer term;

    @NotEmpty
    @Pattern(regexp = "[A-Za-z\\-]{2,30}")
    private String firstName;

    @NotEmpty
    @Pattern(regexp = "[A-Za-z\\-]{2,30}")
    private String lastName;

    @Pattern(regexp = "[A-Za-z\\-]{2,30}")
    private String middleName;

    @NotEmpty
    @Email
    private String email;
    private LocalDate birthdate;

    @NotEmpty
    @Pattern(regexp = "[0-9]{4}")
    private String passportSeries;

    @NotEmpty
    @Pattern(regexp = "[0-9]{4}")
    private String passportNumber;
}
