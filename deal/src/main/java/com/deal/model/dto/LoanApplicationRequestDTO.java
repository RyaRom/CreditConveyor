package com.deal.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Schema(title = "Loan Application Request", description = "Short application info.")
public record LoanApplicationRequestDTO(
        @Schema(description = "Requested loan amount.", example = "1000000", minimum = "10000")
        @NotNull
        @Min(10000)
        BigDecimal amount,

        @Schema(description = "Requested loan term (months).", example = "24", minimum = "6")
        @NotNull
        @Min(6)
        Integer term,

        @Schema(description = "Client's first name", example = "Ivan", pattern = "[A-Za-z\\-]{2,30}")
        @NotNull
        @Pattern(regexp = "[A-Za-z\\-]{2,30}")
        String firstName,

        @Schema(description = "Client's last name", example = "Ivanov", pattern = "[A-Za-z\\-]{2,30}")
        @NotNull
        @Pattern(regexp = "[A-Za-z\\-]{2,30}")
        String lastName,

        @Schema(description = "Client's middle name (if present).", example = "Ivanonovich", pattern = "[A-Za-z]{2,30}")
        @Pattern(regexp = "[A-Za-z]{2,30}")
        String middleName,

        @Schema(description = "Client's email.", example = "iivanov@email.ru")
        @NotNull
        @Email
        String email,

        @Schema(description = "Client's birthdate", example = "1996-11-27", format = "date")
        @NotNull
        LocalDate birthdate,

        @Schema(description = "Client's passport series", example = "1234", pattern = "[0-9]{4}")
        @NotNull
        @Pattern(regexp = "[0-9]{4}")
        String passportSeries,

        @Schema(description = "Client's passport number", example = "123456", pattern = "[0-9]{6}")
        @NotNull
        @Pattern(regexp = "[0-9]{6}")
        String passportNumber
) {
}
