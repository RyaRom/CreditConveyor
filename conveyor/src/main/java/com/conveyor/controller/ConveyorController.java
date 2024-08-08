package com.conveyor.controller;

import com.conveyor.exception.FailedScoringException;
import com.conveyor.model.DTO.CreditDTO;
import com.conveyor.model.DTO.LoanApplicationRequestDTO;
import com.conveyor.model.DTO.LoanOfferDTO;
import com.conveyor.model.DTO.ScoringDataDTO;
import com.conveyor.service.OfferService;
import com.conveyor.service.ScoringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "Credit conveyor", description = "Credit scoring")
@RestController
public class ConveyorController {
    private final OfferService offerService;
    private final ScoringService scoringService;

    public ConveyorController(OfferService offerService, ScoringService scoringService) {
        this.offerService = offerService;
        this.scoringService = scoringService;
    }

    @Operation(summary = "Create offer", description = "creates 4 credit offers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Invalid loan request")
    })
    @PostMapping("/conveyor/offers")
    public ResponseEntity<List<LoanOfferDTO>> createOffer(@Valid @RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.warn("     Invalid data {}        ", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(offerService.createOffers(loanApplicationRequestDTO));
    }

    @Operation(summary = "Credit scoring", description = "Calculates credit parameters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Invalid scoring data")
    })
    @PostMapping("/conveyor/calculation")
    public ResponseEntity<CreditDTO> finalScoring(@Valid @RequestBody ScoringDataDTO scoringData, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.warn("     Invalid data {}        ", bindingResult.getAllErrors());
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
