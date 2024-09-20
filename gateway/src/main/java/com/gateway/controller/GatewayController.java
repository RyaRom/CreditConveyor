package com.gateway.controller;

import com.gateway.feign.ApplicationClient;
import com.gateway.feign.DealClient;
import com.gateway.model.dto.FinishRegistrationRequestDTO;
import com.gateway.model.dto.LoanApplicationRequestDTO;
import com.gateway.model.dto.LoanOfferDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Gateway controller")
public class GatewayController {

    private ApplicationClient applicationClient;

    private DealClient dealClient;

    @PostMapping("/application")
    public ResponseEntity<List<LoanOfferDTO>> generateOffers(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
        return applicationClient.generateOffers(loanApplicationRequestDTO);
    }

    @PostMapping("/application/apply")
    public ResponseEntity<Void> pickOffer(@RequestBody LoanOfferDTO loanOfferDTO) {
        return applicationClient.pickOffer(loanOfferDTO);
    }

    @PutMapping("/application/registration/{applicationId}")
    public ResponseEntity<Void> calculateCredit(@RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO, @PathVariable Long applicationId) {
        return dealClient.calculateCredit(finishRegistrationRequestDTO, applicationId);
    }

    @PostMapping("/document/{applicationId}")
    public ResponseEntity<Void> sendDocuments(@PathVariable Long applicationId) {
        return dealClient.sendDocuments(applicationId);
    }

    @PostMapping("/document/{applicationId}/sign")
    public ResponseEntity<Void> signDocuments(@PathVariable Long applicationId) {
        return dealClient.signDocuments(applicationId);
    }

    @PostMapping("/document/{applicationId}/sign/code")
    public ResponseEntity<Void> verifyCode(@PathVariable Long applicationId, @RequestBody String sesCode) {
        return dealClient.verifyCode(applicationId, sesCode);
    }
}
