package com.deal.model.json;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanOffer {
    private Long applicationId;
    private Double requestedAmount;
    private Double totalAmount;
    private Integer term;
    private Double monthlyPayment;
    private Double rate;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
}
