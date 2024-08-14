package com.conveyor.controller;

import com.conveyor.exception.FailedScoringException;
import com.conveyor.model.dto.CreditDTO;
import com.conveyor.model.dto.LoanApplicationRequestDTO;
import com.conveyor.model.dto.LoanOfferDTO;
import com.conveyor.model.dto.ScoringDataDTO;
import com.conveyor.service.OfferService;
import com.conveyor.service.ScoringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "Credit conveyor", description = "Credit scoring")
@RequiredArgsConstructor
@RestController
public class ConveyorController {
    private final OfferService offerService;
    private final ScoringService scoringService;

    @Operation(summary = "Create offer", description = "creates 4 credit offers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Invalid loan request"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping("/conveyor/offers")
    public ResponseEntity<List<LoanOfferDTO>> createOffer(@Valid @RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
        return ResponseEntity.ok(offerService.createOffers(loanApplicationRequestDTO));
    }

    @Operation(summary = "Credit scoring", description = "Calculates credit parameters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Invalid scoring data"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping("/conveyor/calculation")
    public ResponseEntity<CreditDTO> finalScoring(@Valid @RequestBody ScoringDataDTO scoringData) throws FailedScoringException {
        CreditDTO credit = scoringService.scoring(scoringData);
        return ResponseEntity.ok(credit);
    }
}
