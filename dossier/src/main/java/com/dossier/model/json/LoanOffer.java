package com.dossier.model.json;

import java.math.BigDecimal;

public record LoanOffer(
        Long applicationId,
        BigDecimal requestedAmount,
        BigDecimal totalAmount,
        Integer term,
        BigDecimal monthlyPayment,
        BigDecimal rate,
        Boolean isInsuranceEnabled,
        Boolean isSalaryClient
) {
}

