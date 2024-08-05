package com.conveyor.service;

import com.conveyor.exception.FailedScoringException;
import com.conveyor.model.DTO.CreditDTO;
import com.conveyor.model.DTO.EmploymentDTO;
import com.conveyor.model.DTO.ScoringDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ScoringService {
    @Value("${scoring.base_rate}")
    private static Double BASE_RATE;
    @Value("${scoring.insurance_discount}")
    private static Double INSURANCE_DISCOUNT;
    @Value("${scoring.insurance_cost}")
    private static Double INSURANCE_COST;
    @Value("${scoring.salary_client_discount}")
    private static Double SALARY_CLIENT_DISCOUNT;
    public CreditDTO scoring(ScoringDataDTO scoringData) throws FailedScoringException {
        CreditDTO creditDTO = new CreditDTO();

        Double amount = getTotalAmount(scoringData.getIsInsuranceEnabled(), scoringData.getIsSalaryClient(), scoringData.getAmount());
        Double rate = getRate(scoringData.getIsInsuranceEnabled(), scoringData.getIsSalaryClient());
        rate = calculateFinalRate(scoringData, rate);

        creditDTO.setIsSalaryClient(scoringData.getIsSalaryClient());
        creditDTO.setIsInsuranceEnabled(scoringData.getIsInsuranceEnabled());
        creditDTO.setAmount(amount);
        return creditDTO;
    }
    public Double calculateFinalRate(ScoringDataDTO scoringData, Double baseRate) throws FailedScoringException {
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

        Integer age = getAge(scoringData.getBirthdate());
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
            log.info("Client {} failed scoring: {}", scoringData.getAccount(), message);
            throw new FailedScoringException(message);
        }
        return baseRate;
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
        Double monthlyRate = rate/12;
        Double monthlyPayment = totalAmount*(monthlyRate*Math.pow(1+monthlyRate, term))/(Math.pow(1+monthlyRate, term)-1);
        return monthlyPayment;
    }
}
