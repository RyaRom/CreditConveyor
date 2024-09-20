package com.gateway.model.dto;


import com.gateway.model.enums.Gender;
import com.gateway.model.enums.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(title = "Finish Registration Request", description = "Finish registration request with full client data.")
public record FinishRegistrationRequestDTO(

        @Schema(description = "Client's gender.", example = "MALE")
        @NotNull
        Gender gender,

        @Schema(description = "Client's marital status.", example = "SINGLE")
        @NotNull
        MaritalStatus maritalStatus,

        @Schema(description = "Client's dependents amount.", example = "1")
        @NotNull
        Integer dependentAmount,

        @Schema(description = "Client's passport issue date.", example = "2016-11-27", format = "date")
        @NotNull
        LocalDate passportIssueDate,

        @Schema(description = "Client's passport issue branch.", example = "123-456")
        @NotNull
        String passportIssueBranch,

        @Schema(description = "Client's employment info.")
        @NotNull
        EmploymentDTO employment,

        @Schema(description = "Client's master account.", example = "11223344556677889900", pattern = "[0-9]{20}")
        @NotNull
        @Pattern(regexp = "[0-9]{20}")
        String account
) {
}
