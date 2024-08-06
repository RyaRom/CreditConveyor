package com.conveyor.model.DTO;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreditDTO {
    private Double amount;
    private Integer term;
    private Double monthlyPayment;
    private Double rate;
    private Double psk;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
    private List<PaymentScheduleElement> paymentSchedule;
}
