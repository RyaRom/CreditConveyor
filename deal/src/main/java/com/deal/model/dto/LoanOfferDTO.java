package com.deal.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(title = "Loan Offer", description = "Loan offer after short scoring.")
public class LoanOfferDTO {

    @Schema(description = "Application ID.", example = "1")
    private Long applicationId;

    @Schema(description = "Requested loan amount.", example = "1000000.00", minimum = "10000.00")
    @NotNull
    @Min(10000)
    private Double requestedAmount;

    @Schema(description = "Total loan amount (with services, insurance, etc.).", example = "1000000.00", minimum = "10000.00")
    @NotNull
    @Min(10000)
    private Double totalAmount;

    @Schema(description = "Requested loan term (months).", example = "24", minimum = "6")
    @NotNull
    @Min(6)
    private Integer term;

    @Schema(description = "Monthly payment.", example = "10000.10")
    @NotNull
    private Double monthlyPayment;

    @Schema(description = "Loan rate", example = "12.50")
    @NotNull
    private Double rate;

    @Schema(description = "Is insurance enabled?", example = "true")
    @NotNull
    private Boolean isInsuranceEnabled;

    @Schema(description = "Is salary client?", example = "true")
    @NotNull
    private Boolean isSalaryClient;
}
