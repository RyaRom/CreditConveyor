package com.deal.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Schema(title = "Payment Schedule Element", description = "Payment schedule element.")
public record PaymentScheduleElementDTO(
        @Schema(description = "Payment sequence number.", example = "1")
        Integer number,

        @Schema(description = "Payment date.", example = "2022-06-01", format = "date")
        LocalDate date,

        @Schema(description = "Total payment amount.", example = "12000.00")
        BigDecimal totalPayment,

        @Schema(description = "Interest payment amount.", example = "12000.00")
        BigDecimal interestPayment,

        @Schema(description = "Main debt payment amount.", example = "8000.00")
        BigDecimal debtPayment,

        @Schema(description = "Remaining debt amount.", example = "92000.00")
        BigDecimal remainingDebt
) {
}
