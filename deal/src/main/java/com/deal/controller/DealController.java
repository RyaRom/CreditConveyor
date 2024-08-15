package com.deal.controller;

import com.deal.model.dto.FinishRegistrationRequestDTO;
import com.deal.model.dto.LoanApplicationRequestDTO;
import com.deal.model.dto.LoanOfferDTO;
import com.deal.service.ApplicationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Deal conveyor")
@RequiredArgsConstructor
public class DealController {
    private final ApplicationService applicationService;

    @PostMapping("/deal/application")
    public ResponseEntity<List<LoanOfferDTO>> generateOffers(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
        return ResponseEntity.ok(applicationService.generateOffers(loanApplicationRequestDTO));
    }

    @PutMapping("/deal/offer")
    public ResponseEntity<Void> pickOffer(@RequestBody LoanOfferDTO loanOfferDTO) {
        applicationService.updateApplication(loanOfferDTO);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/deal/calculate/{applicationId}")
    public ResponseEntity<Void> calculateCredit(@RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO, @PathVariable Long applicationId) {
        applicationService.applicationScoring(finishRegistrationRequestDTO, applicationId);
        return ResponseEntity.noContent().build();
    }
}
