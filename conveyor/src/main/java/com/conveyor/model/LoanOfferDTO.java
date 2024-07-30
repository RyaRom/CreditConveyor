package com.conveyor.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoanOfferDTO {
    private Long id;

    @NotEmpty
    @Min(10000)
    private Double requestedAmount;

    @NotEmpty
    @Min(10000)
    private Double totalAmount;

    @NotEmpty
    @Min(6)
    private Integer term;

    @NotEmpty
    private Double monthlyPayment;

    @NotEmpty
    private Double rate;

    @NotEmpty
    private Boolean isInsuranceEnabled;

    @NotEmpty
    private Boolean isSalaryClient;
}
