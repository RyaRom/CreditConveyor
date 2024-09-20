package com.application.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@Schema(title = "Loan Offer", description = "Loan offer after short scoring.")
public record LoanOfferDTO(
        @Schema(description = "Application ID.", example = "1")
        Long applicationId,

        @Schema(description = "Requested loan amount.", example = "1000000.00", minimum = "10000.00")
        @NotNull
        @Min(10000)
        BigDecimal requestedAmount,

        @Schema(description = "Total loan amount (with services, insurance, etc.).", example = "1000000.00", minimum = "10000.00")
        @NotNull
        @Min(10000)
        BigDecimal totalAmount,

        @Schema(description = "Requested loan term (months).", example = "24", minimum = "6")
        @NotNull
        @Min(6)
        Integer term,

        @Schema(description = "Monthly payment.", example = "10000.10")
        @NotNull
        BigDecimal monthlyPayment,

        @Schema(description = "Loan rate", example = "12.50")
        @NotNull
        BigDecimal rate,

        @Schema(description = "Is insurance enabled?", example = "true")
        @NotNull
        Boolean isInsuranceEnabled,

        @Schema(description = "Is salary client?", example = "true")
        @NotNull
        Boolean isSalaryClient
) {
}
