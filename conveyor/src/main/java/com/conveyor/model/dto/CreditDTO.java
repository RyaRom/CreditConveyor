package com.conveyor.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Schema(title = "Credit", description = "Credit's info.")
public record CreditDTO(
        @Schema(description = "Requested loan amount.", example = "1000000.00")
        BigDecimal amount,

        @Schema(description = "Requested loan term (months).", example = "24")
        Integer term,

        @Schema(description = "Monthly payment.", example = "10000.10")
        BigDecimal monthlyPayment,

        @Schema(description = "Loan rate.", example = "12.50")
        BigDecimal rate,

        @Schema(description = "Loan Full Price.", example = "12.50")
        BigDecimal psk,

        @Schema(description = "Is insurance enabled?", example = "true")
        Boolean isInsuranceEnabled,

        @Schema(description = "Is salary client?", example = "true")
        Boolean isSalaryClient,

        @Schema(description = "Payment schedule elements.")
        List<PaymentScheduleElementDTO> paymentSchedule
) {
}
