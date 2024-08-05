package com.conveyor.controller;

import com.conveyor.exception.FailedScoringException;
import com.conveyor.model.DTO.CreditDTO;
import com.conveyor.model.DTO.LoanApplicationRequestDTO;
import com.conveyor.model.DTO.LoanOfferDTO;
import com.conveyor.model.DTO.ScoringDataDTO;
import com.conveyor.service.OfferService;
import com.conveyor.service.ScoringService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class ConveyorController {
    private final OfferService offerService;
    private final ScoringService scoringService;

    public ConveyorController(OfferService offerService, ScoringService scoringService) {
        this.offerService = offerService;
        this.scoringService = scoringService;
    }

    @PostMapping("/conveyor/offers")
    public ResponseEntity<List<LoanOfferDTO>> createOffer(@Valid @RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.error("     Invalid data {}        ", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(offerService.createOffers(loanApplicationRequestDTO));
    }

    @PostMapping("/conveyor/calculation")
    public ResponseEntity<CreditDTO> finalScoring(@Valid @RequestBody ScoringDataDTO scoringData, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.error("     Invalid data {}        ", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().build();
        }
        CreditDTO credit;
        try {
            credit = scoringService.scoring(scoringData);
        } catch (FailedScoringException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(credit);
    }
}
