package com.deal.model.dto;

import com.deal.model.enums.Gender;
import com.deal.model.enums.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@Schema(title = "Scoring Data", description = "Full scoring data.")
public class ScoringDataDTO {

    @Schema(description = "Requested loan amount.", example = "1000000", minimum = "10000")
    @NotNull
    @Min(10000)
    private BigDecimal amount;

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

    @Schema(description = "Client's gender.", example = "MALE")
    private Gender gender;

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

    @Schema(description = "Client's passport issue date.", example = "2016-11-27", format = "date")
    private LocalDate passportIssueDate;

    @Schema(description = "Client's passport issue branch.", example = "123-456")
    private String passportIssueBranch;

    @Schema(description = "Client's marital status.", example = "SINGLE")
    private MaritalStatus maritalStatus;

    @Schema(description = "Client's dependents amount.", example = "1")
    private Integer dependentAmount;

    @Schema(description = "Client's employment info.")
    private EmploymentDTO employment;

    @Schema(description = "Client's master account.", example = "11223344556677889900", pattern = "[0-9]{20}")
    @Pattern(regexp = "[0-9]{20}")
    private String account;

    @Schema(description = "Is insurance enabled?", example = "true")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Is salary client?", example = "true")
    private Boolean isSalaryClient;

}
