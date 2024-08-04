package com.conveyor.model.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PaymentScheduleElementDTO {
    private Integer number;
    private LocalDate date;
    private Double totalPayment;
    private Double interestPayment;
    private Double debtPayment;
    private Double remainingDebt;
}
