package com.conveyor.model.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanOfferDTO {
    private Long id;

    @NotNull
    @Min(10000)
    private Double requestedAmount;

    @NotNull
    @Min(10000)
    private Double totalAmount;

    @NotNull
    @Min(6)
    private Integer term;

    @NotNull
    private Double monthlyPayment;

    @NotNull
    private Double rate;

    @NotNull
    private Boolean isInsuranceEnabled;

    @NotNull
    private Boolean isSalaryClient;
}
