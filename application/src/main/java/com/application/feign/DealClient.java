package com.application.feign;

import com.application.model.dto.LoanApplicationRequestDTO;
import com.application.model.dto.LoanOfferDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "deal-client", configuration = FeignConfig.class, url = "${feign.url")
public interface DealClient {
    @PostMapping("/deal/application")
    List<LoanOfferDTO> createOffers(LoanApplicationRequestDTO loanApplicationRequestDTO);

    @PostMapping("/deal/offer")
    Void updateApplication(LoanOfferDTO loanOfferDTO);
}
