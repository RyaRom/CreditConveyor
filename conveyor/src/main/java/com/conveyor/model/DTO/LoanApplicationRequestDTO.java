package com.conveyor.model.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@Schema(title = "Loan Application Request", description = "Short application info.")
public class LoanApplicationRequestDTO {

    @Schema(description = "Requested loan amount.", example = "1000000", minimum = "10000")
    @NotNull
    @Min(10000)
    private Double amount;

    @Schema(description = "Requested loan term (months).", example = "24", minimum = "6")
    @NotNull
    @Min(6)
    private Integer term;

    @Schema(description = "Client's first name", example = "Ivan", pattern = "[A-Za-z\\-]{2,30}")
    @NotNull
    @Pattern(regexp = "[A-Za-z\\-]{2,30}")
    private String firstName;

    @Schema(description = "Client's last name", example = "Ivanov", pattern = "[A-Za-z\\-]{2,30}")
    @NotNull
    @Pattern(regexp = "[A-Za-z\\-]{2,30}")
    private String lastName;

    @Schema(description = "Client's middle name (if present).", example = "Ivanonovich", pattern = "[A-Za-z]{2,30}")
    @Pattern(regexp = "[A-Za-z]{2,30}")
    private String middleName;

    @Schema(description = "Client's email.", example = "iivanov@email.ru")
    @NotNull
    @Email
    private String email;

    @Schema(description = "Client's birthdate", example = "1996-11-27", format = "date")
    @NotNull
    private LocalDate birthdate;

    @Schema(description = "Client's passport series", example = "1234", pattern = "[0-9]{4}")
    @NotNull
    @Pattern(regexp = "[0-9]{4}")
    private String passportSeries;

    @Schema(description = "Client's passport number", example = "123456", pattern = "[0-9]{6}")
    @NotNull
    @Pattern(regexp = "[0-9]{6}")
    private String passportNumber;

}