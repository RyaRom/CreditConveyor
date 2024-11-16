package com.application.controller;

import com.application.model.dto.LoanApplicationRequestDTO;
import com.application.model.dto.LoanOfferDTO;
import com.application.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @PostMapping("/application")
    public ResponseEntity<List<LoanOfferDTO>> generateOffers(@Valid @RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
        return ResponseEntity.ok(applicationService.generateOffers(loanApplicationRequestDTO));
    }

    @PostMapping("/application/offer")
    public ResponseEntity<Void> pickOffer(@Validated @RequestBody LoanOfferDTO loanOfferDTO) {
        applicationService.updateApplication(loanOfferDTO);
        return ResponseEntity.noContent().build();
    }

}
