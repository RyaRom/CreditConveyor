package com.gateway.feign;

import com.gateway.model.dto.LoanApplicationRequestDTO;
import com.gateway.model.dto.LoanOfferDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "application-client", configuration = FeignConfig.class, url = "${feign.application-url")
public interface ApplicationClient {
    @PostMapping("/application")
    ResponseEntity<List<LoanOfferDTO>> generateOffers(LoanApplicationRequestDTO loanApplicationRequestDTO);

    @PostMapping("/application/offer")
    ResponseEntity<Void> pickOffer(LoanOfferDTO loanOfferDTO);
}
