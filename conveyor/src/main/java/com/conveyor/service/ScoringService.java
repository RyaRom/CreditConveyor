package com.conveyor.service;

import com.conveyor.model.DTO.CreditDTO;
import com.conveyor.model.DTO.ScoringDataDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    public CreditDTO scoring(ScoringDataDTO scoringData){
        CreditDTO creditDTO = new CreditDTO();

        //scoring

        return creditDTO;
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
