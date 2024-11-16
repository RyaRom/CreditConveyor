package com.dossier.client;


import com.dossier.config.FeignConfig;
import com.dossier.model.dto.ApplicationDTO;
import com.dossier.model.dto.FinishRegistrationRequestDTO;
import com.dossier.model.dto.LoanApplicationRequestDTO;
import com.dossier.model.dto.LoanOfferDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "deal-client", configuration = FeignConfig.class, url = "${feign.url}")
public interface DealClient {

    @GetMapping("/application/{applicationId}")
    ApplicationDTO getApplicationById(@PathVariable("applicationId") Long applicationId);

    @GetMapping("/application/all")
    List<ApplicationDTO> getAllApplications();

    @PutMapping("/application/{applicationId}/status")
    void updateApplicationStatusById(@PathVariable("applicationId") Long applicationId, @RequestParam("statusName") String statusName);

    @PostMapping("/deal/application")
    ResponseEntity<List<LoanOfferDTO>> generateOffers(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO);

    @PutMapping("/deal/offer")
    ResponseEntity<Void> pickOffer(@RequestBody LoanOfferDTO loanOfferDTO);

    @PutMapping("/deal/calculate/{applicationId}")
    ResponseEntity<Void> calculateCredit(@RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO, @PathVariable("applicationId") Long applicationId);

    @DeleteMapping("/application/{applicationId}/delete")
    void deleteApplicationById(@PathVariable("applicationId") Long applicationId);
}