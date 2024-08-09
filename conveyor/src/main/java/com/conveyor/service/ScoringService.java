package com.conveyor.service;

import com.conveyor.exception.FailedScoringException;
import com.conveyor.model.DTO.CreditDTO;
import com.conveyor.model.DTO.EmploymentDTO;
import com.conveyor.model.DTO.PaymentScheduleElementDTO;
import com.conveyor.model.DTO.ScoringDataDTO;
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
    @Value("${scoring.base_rate}")
    private Double BASE_RATE;
    @Value("${scoring.insurance_discount}")
    private Double INSURANCE_DISCOUNT;
    @Value("${scoring.insurance_cost}")
    private Double INSURANCE_COST;
    @Value("${scoring.salary_client_discount}")
    private Double SALARY_CLIENT_DISCOUNT;
    public CreditDTO scoring(ScoringDataDTO scoringData) throws FailedScoringException {
        Double rate = getRate(scoringData.getIsInsuranceEnabled(), scoringData.getIsSalaryClient());
        rate = calculateFinalRate(scoringData, rate);
        Double totalAmount = getTotalAmount(
                scoringData.getIsInsuranceEnabled(),
                scoringData.getIsSalaryClient(),
                scoringData.getAmount()
        );
        Double monthlyPayment = round(getMonthlyPayment(totalAmount, rate, scoringData.getTerm()));
        List<PaymentScheduleElementDTO> payments = getPayments(totalAmount, rate, monthlyPayment, scoringData.getTerm());
        Double psk = getPSK(payments, scoringData.getAmount(), scoringData.getTerm());

        CreditDTO credit = CreditDTO.builder()
                .amount(totalAmount)
                .isSalaryClient(scoringData.getIsSalaryClient())
                .isInsuranceEnabled(scoringData.getIsInsuranceEnabled())
                .term(scoringData.getTerm())
                .rate(rate)
                .psk(round(psk))
                .monthlyPayment(monthlyPayment)
                .paymentSchedule(payments)
                .build();
        log.info("\n  Credit created {}   \n For person {} ", credit.toString(), scoringData.getAccount());
        log.info("\n  Payment schedule {}  ", payments);
        return credit;
    }
    protected Double round(Double value){
        BigDecimal bigDecimalValue = new BigDecimal(value);
        BigDecimal roundedValue = bigDecimalValue.setScale(2, RoundingMode.UP);
        return roundedValue.doubleValue();
    }
    private Double calculateFinalRate(ScoringDataDTO scoringData, Double baseRate) throws FailedScoringException {
        EmploymentDTO employment = scoringData.getEmployment();
        List<String> refuseReasons = new ArrayList<>();
        switch (employment.getEmploymentStatus()){
            case UNEMPLOYED -> refuseReasons.add("Unemployment");
            case SELF_EMPLOYED -> baseRate++;
            case BUSINESS_OWNER -> baseRate+=3;
        }
        switch (employment.getPosition()){
            case MID_MANAGER -> baseRate -=2;
            case TOP_MANAGER -> baseRate -=4;
        }
        if (scoringData.getAmount()>employment.getSalary()*20)refuseReasons.add("Loan amount is too big");
        switch (scoringData.getMaritalStatus()){
            case MARRIED -> baseRate-=3;
            case DIVORCED -> baseRate++;
        }
        if (scoringData.getDependentAmount()>1)baseRate++;

        int age = getAge(scoringData.getBirthdate());
        if (age > 60 || age < 20){
            refuseReasons.add("Too old or young");
        }
        switch (scoringData.getGender()){
            case MALE -> {
                if (age >=30 && age <=55)baseRate-=3;
            }
            case FEMALE -> {
                if (age >=35 && age <=60)baseRate-=3;
            }
            case NON_BINARY ->baseRate+=3;
        }
        if (employment.getWorkExperienceCurrent() < 3) refuseReasons.add("Not enough current work experience");
        if (employment.getWorkExperienceTotal() < 12) refuseReasons.add("Not enough work experience");
        if (!refuseReasons.isEmpty()) {
            String message = String.join("\n", refuseReasons);
            log.error("Client {} failed scoring: {}", scoringData.getAccount(), message);
            throw new FailedScoringException(message);
        }
        return baseRate;
    }
    private Double getPSK(List<PaymentScheduleElementDTO> payments, Double requestedAmount, Integer term){
        Double paymentSum = payments.stream().map(PaymentScheduleElementDTO::getTotalPayment).reduce(Double::sum).orElse(0.0);
        return 100*(paymentSum/requestedAmount-1)/(term/12);
    }
    private List<PaymentScheduleElementDTO> getPayments(Double totalAmount, Double rate, Double monthlyPayment , Integer term){
        Double remainingDebt = totalAmount;
        LocalDate date = LocalDate.now();
        List<PaymentScheduleElementDTO> payments = new ArrayList<>(List.of(
                PaymentScheduleElementDTO.builder()
                        .number(0)
                        .date(date)
                        .remainingDebt(remainingDebt)
                        .totalPayment(0.0)
                        .debtPayment(0.0)
                        .interestPayment(0.0)
                        .build()
        ));
        for (int i = 1; i <= term; i++) {
            date = date.plusMonths(1);
            Double interestPayment = round(remainingDebt * (rate/100)/12);
            Double debtPayment = round(remainingDebt-interestPayment);
            remainingDebt = round(remainingDebt-debtPayment);
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
    private Integer getAge(LocalDate birthdate){
        return LocalDate.now().getYear()-birthdate.getYear();
    }
    public Double getRate(Boolean isInsuranceEnabled, Boolean isSalaryClient){
        Double rate = BASE_RATE;
        rate -= isInsuranceEnabled? INSURANCE_DISCOUNT:0;
        rate -= isSalaryClient? SALARY_CLIENT_DISCOUNT:0;
        return rate;
    }
    public Double getTotalAmount(Boolean isInsuranceEnabled, Boolean isSalaryClient, Double requestedAmount){
        if (isInsuranceEnabled){
            if (isSalaryClient) return requestedAmount;
            else return requestedAmount+INSURANCE_COST;
        }
        else return requestedAmount;
    }
    public Double getMonthlyPayment(Double totalAmount, Double rate, Integer term){
        rate/=100;
        double monthlyRate = rate/12;
        return totalAmount*(monthlyRate*Math.pow(1+monthlyRate, term))/(Math.pow(1+monthlyRate, term)-1);
    }
}
