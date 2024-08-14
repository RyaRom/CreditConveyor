package com.deal.service;

import com.deal.config.ConveyorClient;
import com.deal.model.dto.LoanApplicationRequestDTO;
import com.deal.model.dto.LoanOfferDTO;
import com.deal.model.entities.Application;
import com.deal.model.entities.Client;
import com.deal.model.mapping.ApplicationRequestMapper;
import com.deal.repo.ApplicationRepo;
import com.deal.repo.ClientRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DealService {
    private final ConveyorClient conveyorClient;
    private final ApplicationRequestMapper applicationRequestMapper;
    private final ClientRepo clientRepo;
    private final ApplicationRepo applicationRepo;

    public List<LoanOfferDTO> generateOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        Client client = applicationRequestMapper.toClient(loanApplicationRequestDTO);
        Application application = new Application();
        application.setClient_id(client);

        clientRepo.save(client);
        applicationRepo.save(application);

        List<LoanOfferDTO> offers = conveyorClient.createOffers(loanApplicationRequestDTO);
        log.info("Response from conveyor MC: {}", offers);
        Long id = application.getApplication_id();
        offers.forEach(offer->offer.setApplicationId(id));

        return offers;
    }
}
