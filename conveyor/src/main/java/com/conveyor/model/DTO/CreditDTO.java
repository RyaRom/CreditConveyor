package com.conveyor.model.DTO;

import lombok.Data;

@Data
public class CreditDTO {
    private Double amount;
    private Integer term;
    private Double monthlyPayment;
    private Double rate;
    private Double psk;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
    private PaymentScheduleElementDTO paymentSchedule;
}
