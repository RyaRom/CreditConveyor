package com.deal.client;

import com.deal.config.FeignConfig;
import com.deal.model.dto.CreditDTO;
import com.deal.model.dto.LoanApplicationRequestDTO;
import com.deal.model.dto.LoanOfferDTO;
import com.deal.model.dto.ScoringDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "conveyor-client", configuration = FeignConfig.class, url = "${feign.url}")
public interface ConveyorClient {
    @PostMapping("/conveyor/offers")
    List<LoanOfferDTO> createOffers(LoanApplicationRequestDTO loanApplicationRequestDTO);

    @PostMapping("/conveyor/calculation")
    CreditDTO calculateCredit(ScoringDataDTO scoringData);
}
