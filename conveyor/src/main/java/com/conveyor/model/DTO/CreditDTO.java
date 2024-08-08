package com.conveyor.model.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreditDTO {
    @NotNull
    private Double amount;
    @NotNull
    private Integer term;
    @NotNull
    private Double monthlyPayment;
    @NotNull
    private Double rate;
    @NotNull
    private Double psk;
    @NotNull
    private Boolean isInsuranceEnabled;
    @NotNull
    private Boolean isSalaryClient;
    @NotEmpty
    private List<PaymentScheduleElement> paymentSchedule;
}
