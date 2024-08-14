package com.deal.controller;

import com.deal.model.dto.LoanApplicationRequestDTO;
import com.deal.model.dto.LoanOfferDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DealController {
    @PostMapping("/deal/application")
    public ResponseEntity<List<LoanOfferDTO>> generateOffers(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {

    }
}
