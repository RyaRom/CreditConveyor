package com.deal.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@Schema(title = "Payment Schedule Element", description = "Payment schedule element.")
public class PaymentScheduleElementDTO {

    @Schema(description = "Payment sequence number.", example = "1")
    private Integer number;

    @Schema(description = "Payment date.", example = "2022-06-01", format = "date")
    private LocalDate date;

    @Schema(description = "Total payment amount.", example = "12000.00")
    private Double totalPayment;

    @Schema(description = "Interest payment amount.", example = "12000.00")
    private Double interestPayment;

    @Schema(description = "Main debt payment amount.", example = "8000.00")
    private Double debtPayment;

    @Schema(description = "Remaining debt amount.", example = "92000.00")
    private Double remainingDebt;

    @Override
    public String toString() {
        return "PaymentScheduleElementDTO{" +
                "number=" + number +
                "\n date=" + date +
                "\n totalPayment=" + totalPayment +
                "\n interestPayment=" + interestPayment +
                "\n debtPayment=" + debtPayment +
                "\n remainingDebt=" + remainingDebt +
                "}\n";
    }
}
