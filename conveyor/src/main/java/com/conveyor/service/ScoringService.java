package com.conveyor.service;

import com.conveyor.exception.FailedScoringException;
import com.conveyor.model.dto.CreditDTO;
import com.conveyor.model.dto.EmploymentDTO;
import com.conveyor.model.dto.PaymentScheduleElementDTO;
import com.conveyor.model.dto.ScoringDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ScoringService {

    @Value("${scoring.base-rate}")
    private BigDecimal baseRate;

    @Value("${scoring.insurance-discount}")
    private BigDecimal insuranceDiscount;

    @Value("${scoring.insurance-cost}")
    private BigDecimal insuranceCost;

    @Value("${scoring.salary-client-discount}")
    private BigDecimal salaryClientDiscount;

    public CreditDTO scoring(ScoringDataDTO scoringData) throws FailedScoringException {
        BigDecimal rate = getBaseRate(scoringData.isInsuranceEnabled(), scoringData.isSalaryClient());
        rate = calculateFinalRate(scoringData, rate);
        BigDecimal totalAmount = getTotalAmount(
                scoringData.isInsuranceEnabled(),
                scoringData.isSalaryClient(),
                scoringData.amount()
        );
        BigDecimal monthlyPayment = getMonthlyPayment(totalAmount, rate, scoringData.term());
        List<PaymentScheduleElementDTO> payments = getPayments(totalAmount, rate, monthlyPayment, scoringData.term());
        BigDecimal psk = getPSK(payments, scoringData.amount(), scoringData.term());

        CreditDTO credit = CreditDTO.builder()
                .amount(totalAmount)
                .isSalaryClient(scoringData.isSalaryClient())
                .isInsuranceEnabled(scoringData.isInsuranceEnabled())
                .term(scoringData.term())
                .rate(rate)
                .psk(round(psk))
                .monthlyPayment(monthlyPayment)
                .paymentSchedule(payments)
                .build();
        log.info("Credit created {} For person {} ", credit.toString(), scoringData.account());
        log.info("Payment schedule {}", payments);
        return credit;
    }

    protected BigDecimal round(BigDecimal value) {
        if (Math.abs(value.doubleValue()) < 1) return BigDecimal.valueOf(0.0);
        return value.setScale(2, RoundingMode.UP);
    }

    private BigDecimal calculateFinalRate(ScoringDataDTO scoringData, BigDecimal baseRate) throws FailedScoringException {
        EmploymentDTO employment = scoringData.employment();
        List<String> refuseReasons = new ArrayList<>();
        switch (employment.employmentStatus()) {
            case UNEMPLOYED -> refuseReasons.add("Unemployment");
            case SELF_EMPLOYED -> baseRate = baseRate.add(BigDecimal.valueOf(1));
            case BUSINESS_OWNER -> baseRate = baseRate.add(BigDecimal.valueOf(3));
        }
        switch (employment.position()) {
            case MID_MANAGER -> baseRate = baseRate.subtract(BigDecimal.valueOf(2));
            case TOP_MANAGER -> baseRate = baseRate.subtract(BigDecimal.valueOf(4));
        }
        if (scoringData.amount().doubleValue() > employment.salary().doubleValue() * 20)
            refuseReasons.add("Loan amount is too big");
        switch (scoringData.maritalStatus()) {
            case MARRIED -> baseRate = baseRate.subtract(BigDecimal.valueOf(3));
            case DIVORCED -> baseRate = baseRate.add(BigDecimal.valueOf(1));
        }
        if (scoringData.dependentAmount() > 1) baseRate = baseRate.add(BigDecimal.valueOf(1));

        int age = getAge(scoringData.birthdate());
        if (age > 60 || age < 20) {
            refuseReasons.add("Too old or young");
        }
        switch (scoringData.gender()) {
            case MALE -> {
                if (age >= 30 && age <= 55) baseRate = baseRate.subtract(BigDecimal.valueOf(3));
            }
            case FEMALE -> {
                if (age >= 35 && age <= 60) baseRate = baseRate.subtract(BigDecimal.valueOf(3));
            }
            case NON_BINARY -> baseRate = baseRate.add(BigDecimal.valueOf(3));
        }
        if (employment.workExperienceCurrent() < 3) refuseReasons.add("Not enough current work experience");
        if (employment.workExperienceTotal() < 12) refuseReasons.add("Not enough work experience");
        if (!refuseReasons.isEmpty()) {
            String message = "Client " + scoringData.account() + " "
                    + String.join(", ", refuseReasons);
            throw new FailedScoringException(message);
        }
        return baseRate;
    }

    private BigDecimal getPSK(List<PaymentScheduleElementDTO> payments, BigDecimal requestedAmount, Integer term) {
        BigDecimal paymentSum = payments.stream()
                .map(PaymentScheduleElementDTO::totalPayment)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.valueOf(0.0));
        BigDecimal psk = BigDecimal.valueOf(100 * (paymentSum.divide(requestedAmount).doubleValue() - 1) / (term / 12));
        log.info("PSK = {}", psk);
        return psk;
    }

    private List<PaymentScheduleElementDTO> getPayments(BigDecimal totalAmount, BigDecimal rate, BigDecimal monthlyPayment, Integer term) {
        BigDecimal remainingDebt = totalAmount;
        LocalDate date = LocalDate.now();
        List<PaymentScheduleElementDTO> payments = new ArrayList<>(List.of(
                PaymentScheduleElementDTO.builder()
                        .number(0)
                        .date(date)
                        .remainingDebt(remainingDebt)
                        .totalPayment(BigDecimal.valueOf(0.0))
                        .debtPayment(BigDecimal.valueOf(0.0))
                        .interestPayment(BigDecimal.valueOf(0.0))
                        .build()
        ));
        for (int i = 1; i <= term; i++) {
            date = date.plusMonths(1);
            BigDecimal interestPayment = round(
                    remainingDebt.multiply(rate)
                            .divide(BigDecimal.valueOf(100),
                                    RoundingMode.HALF_UP)
                            .divide(BigDecimal.valueOf(12),
                                    RoundingMode.HALF_UP));
            BigDecimal debtPayment = round(monthlyPayment.subtract(interestPayment));
            remainingDebt = round(remainingDebt.subtract(debtPayment));
            payments.add(PaymentScheduleElementDTO
                    .builder()
                    .date(date)
                    .number(i)
                    .totalPayment(monthlyPayment)
                    .debtPayment(debtPayment)
                    .interestPayment(interestPayment)
                    .remainingDebt(remainingDebt)
                    .build());
        }
        return payments;
    }

    private Integer getAge(LocalDate birthdate) {
        return LocalDate.now().getYear() - birthdate.getYear();
    }

    public BigDecimal getBaseRate(Boolean isInsuranceEnabled, Boolean isSalaryClient) {
        double rate = baseRate.doubleValue();
        rate -= isInsuranceEnabled ? insuranceDiscount.doubleValue() : 0;
        rate -= isSalaryClient ? salaryClientDiscount.doubleValue() : 0;
        return BigDecimal.valueOf(rate);
    }

    public BigDecimal getTotalAmount(Boolean isInsuranceEnabled, Boolean isSalaryClient, BigDecimal requestedAmount) {
        if (isInsuranceEnabled) {
            if (isSalaryClient) return requestedAmount;
            else return requestedAmount.add(insuranceCost);
        } else return requestedAmount;
    }

    public BigDecimal getMonthlyPayment(BigDecimal totalAmount, BigDecimal rate, Integer term) {
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        BigDecimal onePlusMonthlyRate = BigDecimal.ONE.add(monthlyRate);
        BigDecimal numerator = monthlyRate.multiply(onePlusMonthlyRate.pow(term));
        BigDecimal denominator = onePlusMonthlyRate.pow(term).subtract(BigDecimal.ONE);
        BigDecimal result = totalAmount.multiply(numerator.divide(denominator, 10, RoundingMode.HALF_UP));

        return round(result);
    }
}
