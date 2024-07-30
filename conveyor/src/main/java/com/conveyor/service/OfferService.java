package com.conveyor.service;

import com.conveyor.model.LoanApplicationRequestDTO;
import com.conveyor.model.LoanOfferDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class OfferService {
    @Value("${scoring.base_rate}")
    private static Double BASE_RATE;
    @Value("${scoring.insurance_discount}")
    private static Double INSURANCE_DISCOUNT;
    @Value("${scoring.insurance_cost}")
    private static Double INSURANCE_COST;
    @Value("${scoring.salary_client_discount}")
    private static Double SALARY_CLIENT_DISCOUNT;

    public List<LoanOfferDTO> createOffers(LoanApplicationRequestDTO loanApplicationRequestDTO){
        List<LoanOfferDTO> offers = new ArrayList<>(){{
            add(generateOffer(loanApplicationRequestDTO, true, true));
            add(generateOffer(loanApplicationRequestDTO, true, false));
            add(generateOffer(loanApplicationRequestDTO, false, true));
            add(generateOffer(loanApplicationRequestDTO, false, false));
        }};
        offers.sort(Comparator.comparingDouble(LoanOfferDTO::getRate));
        return offers;
    }
    public LoanOfferDTO generateOffer(LoanApplicationRequestDTO loanApplicationRequestDTO, boolean isInsuranceEnabled, boolean isSalaryClient ){
        LoanOfferDTO offer = new LoanOfferDTO();
        offer.setRequestedAmount(loanApplicationRequestDTO.getAmount());
        offer.setTerm(loanApplicationRequestDTO.getTerm());
        offer.setIsInsuranceEnabled(isInsuranceEnabled);
        offer.setIsSalaryClient(isSalaryClient);
        Double rate = getRate(isInsuranceEnabled, isSalaryClient);
        offer.setRate(rate);
        Double totalAmount = getTotalAmount(isInsuranceEnabled, isSalaryClient, loanApplicationRequestDTO.getAmount());
        offer.setTotalAmount(totalAmount);
        offer.setMonthlyPayment(
            getMonthlyPayment(totalAmount, rate, loanApplicationRequestDTO.getTerm())
        );

        return offer;
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
