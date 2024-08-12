package com.conveyor.service;

import com.conveyor.model.dto.LoanApplicationRequestDTO;
import com.conveyor.model.dto.LoanOfferDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OfferService {
    private final ScoringService scoringService;

    public List<LoanOfferDTO> createOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        List<LoanOfferDTO> offers = new ArrayList<>(List.of(
                generateOffer(loanApplicationRequestDTO, true, true),
                generateOffer(loanApplicationRequestDTO, true, false),
                generateOffer(loanApplicationRequestDTO, false, true),
                generateOffer(loanApplicationRequestDTO, false, false)
        ));
        offers = offers.stream().sorted(Comparator.comparingDouble(LoanOfferDTO::getRate).reversed()).toList();
        return offers;
    }

    public LoanOfferDTO generateOffer(LoanApplicationRequestDTO loanApplicationRequestDTO, boolean isInsuranceEnabled, boolean isSalaryClient) {
        Double rate = scoringService.getRate(isInsuranceEnabled, isSalaryClient);
        Double totalAmount = scoringService.getTotalAmount(isInsuranceEnabled, isSalaryClient, loanApplicationRequestDTO.getAmount());
        Double monthlyPayment = scoringService.getMonthlyPayment(totalAmount, rate, loanApplicationRequestDTO.getTerm());
        LoanOfferDTO loanOffer = LoanOfferDTO.builder()
                .requestedAmount(loanApplicationRequestDTO.getAmount())
                .term(loanApplicationRequestDTO.getTerm())
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .rate(rate)
                .totalAmount(totalAmount)
                .monthlyPayment(scoringService.round(monthlyPayment))
                .build();
        log.info("\nLoan offer crated {}", loanOffer.toString());
        return loanOffer;
    }

}
