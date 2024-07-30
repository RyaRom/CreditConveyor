package com.conveyor.controller;

import com.conveyor.model.LoanApplicationRequestDTO;
import com.conveyor.model.LoanOfferDTO;
import com.conveyor.service.OfferService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ConveyorController {
    private final OfferService offerService;

    public ConveyorController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping("/conveyor/offers")
    public List<LoanOfferDTO> createOffer(@Valid @RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO){
        return offerService.createOffers(loanApplicationRequestDTO);
    }
}
