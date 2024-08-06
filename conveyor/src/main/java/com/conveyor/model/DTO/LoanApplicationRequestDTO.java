package com.conveyor.model.DTO;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class LoanApplicationRequestDTO {
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
