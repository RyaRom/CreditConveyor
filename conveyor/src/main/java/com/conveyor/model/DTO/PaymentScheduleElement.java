package com.conveyor.model.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PaymentScheduleElement {
    private Integer number;
    private LocalDate date;
    private Double totalPayment;
    private Double interestPayment;
    private Double debtPayment;
    private Double remainingDebt;
}
