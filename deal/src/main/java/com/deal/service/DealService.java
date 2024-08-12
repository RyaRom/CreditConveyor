package com.deal.service;

import com.deal.model.DTO.LoanApplicationRequestDTO;
import com.deal.model.DTO.LoanOfferDTO;
import com.deal.model.Entities.Application;
import com.deal.model.Entities.Client;
import com.deal.model.mapping.ApplicationRequestMapper;
import com.deal.repo.ApplicationRepo;
import com.deal.repo.ClientRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DealService {
    private final ApplicationRequestMapper applicationRequestMapper;
    private final ClientRepo clientRepo;
    private final ApplicationRepo applicationRepo;

    public DealService(ApplicationRequestMapper applicationRequestMapper, ClientRepo clientRepo, ApplicationRepo applicationRepo) {
        this.applicationRequestMapper = applicationRequestMapper;
        this.clientRepo = clientRepo;
        this.applicationRepo = applicationRepo;
    }

    public List<LoanOfferDTO> generateOffers(LoanApplicationRequestDTO loanApplicationRequestDTO){
        Client client = applicationRequestMapper.toClient(loanApplicationRequestDTO);
        Application application = new Application();
        application.setClient_id(client);
        clientRepo.save(client);
        applicationRepo.save(application);
    }
}
