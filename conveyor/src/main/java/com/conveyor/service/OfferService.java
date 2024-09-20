package com.conveyor.service;

import com.conveyor.model.dto.LoanApplicationRequestDTO;
import com.conveyor.model.dto.LoanOfferDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

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
        offers = offers.stream()
                .sorted(Comparator.comparing((Function<LoanOfferDTO, Double>) offer -> offer.rate().doubleValue()).reversed())
                .toList();
        return offers;
    }

    public LoanOfferDTO generateOffer(LoanApplicationRequestDTO loanApplicationRequestDTO, boolean isInsuranceEnabled, boolean isSalaryClient) {
        BigDecimal rate = scoringService.getBaseRate(isInsuranceEnabled, isSalaryClient);
        BigDecimal totalAmount = scoringService.getTotalAmount(isInsuranceEnabled, isSalaryClient, loanApplicationRequestDTO.amount());
        BigDecimal monthlyPayment = scoringService.getMonthlyPayment(totalAmount, rate, loanApplicationRequestDTO.term());
        LoanOfferDTO loanOffer = LoanOfferDTO.builder()
                .requestedAmount(loanApplicationRequestDTO.amount())
                .term(loanApplicationRequestDTO.term())
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .rate(rate)
                .totalAmount(totalAmount)
                .monthlyPayment(scoringService.round(monthlyPayment))
                .build();
        log.info("Loan offer created {}", loanOffer.toString());
        return loanOffer;
    }

}
