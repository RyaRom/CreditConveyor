package com.deal.model.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(title = "Credit", description = "Credit's info.")
public class CreditDTO {

    @Schema(description = "Requested loan amount.", example = "1000000.00")
    private Double amount;

    @Schema(description = "Requested loan term (months).", example = "24")
    private Integer term;

    @Schema(description = "Monthly payment.", example = "10000.10")
    private Double monthlyPayment;

    @Schema(description = "Loan rate.", example = "12.50")
    private Double rate;

    @Schema(description = "Loan Full Price.", example = "12.50")
    private Double psk;

    @Schema(description = "Is insurance enabled?", example = "true")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Is salary client?", example = "true")
    private Boolean isSalaryClient;

    @Schema(description = "Payment schedule elements.")
    private List<PaymentScheduleElementDTO> paymentSchedule;

    @Override
    public String toString() {
        return "CreditDTO{" +
                "amount=" + amount +
                "\n term=" + term +
                "\n monthlyPayment=" + monthlyPayment +
                "\n rate=" + rate +
                "\n psk=" + psk +
                "\n isInsuranceEnabled=" + isInsuranceEnabled +
                "\n isSalaryClient=" + isSalaryClient +
                '}';
    }
}
