package com.conveyor.service;

import com.conveyor.model.DTO.LoanApplicationRequestDTO;
import com.conveyor.model.DTO.LoanOfferDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class OfferService {
    private final ScoringService scoringService;

    public OfferService(ScoringService scoringService) {
        this.scoringService = scoringService;
    }

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
        Double rate = scoringService.getRate(isInsuranceEnabled, isSalaryClient);
        Double totalAmount = scoringService.getTotalAmount(isInsuranceEnabled, isSalaryClient, loanApplicationRequestDTO.getAmount());
        Double monthlyPayment = scoringService.getMonthlyPayment(totalAmount, rate, loanApplicationRequestDTO.getTerm());
        return LoanOfferDTO.builder()
                .requestedAmount(loanApplicationRequestDTO.getAmount())
                .term(loanApplicationRequestDTO.getTerm())
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .rate(rate)
                .totalAmount(totalAmount)
                .monthlyPayment(monthlyPayment)
                .build();
    }

}
