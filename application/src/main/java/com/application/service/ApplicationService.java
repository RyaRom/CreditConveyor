package com.application.service;

import com.application.feign.DealClient;
import com.application.model.dto.LoanApplicationRequestDTO;
import com.application.model.dto.LoanOfferDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final DealClient dealClient;

    public List<LoanOfferDTO> generateOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        return dealClient.createOffers(loanApplicationRequestDTO);
    }

    public void updateApplication(LoanOfferDTO loanOfferDTO) {
        dealClient.updateApplication(loanOfferDTO);
    }
}
