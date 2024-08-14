package com.deal.service;

import com.deal.config.ConveyorClient;
import com.deal.model.dto.LoanApplicationRequestDTO;
import com.deal.model.dto.LoanOfferDTO;
import com.deal.model.entities.Application;
import com.deal.model.entities.Client;
import com.deal.model.enums.ApplicationStatus;
import com.deal.model.enums.ChangeType;
import com.deal.model.json.LoanOffer;
import com.deal.model.json.StatusHistory;
import com.deal.model.mapping.ApplicationRequestMapper;
import com.deal.repo.ApplicationRepo;
import com.deal.repo.ClientRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationService {
    private final ConveyorClient conveyorClient;
    private final ApplicationRequestMapper applicationRequestMapper;
    private final ClientRepo clientRepo;
    private final ApplicationRepo applicationRepo;

    public List<LoanOfferDTO> generateOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        Client client = applicationRequestMapper.toClient(loanApplicationRequestDTO);
        Application application = createApplication(client);

        clientRepo.save(client);
        applicationRepo.save(application);

        List<LoanOfferDTO> offers = conveyorClient.createOffers(loanApplicationRequestDTO);
        log.info("Response from conveyor MC: {}", offers);
        Long id = application.getApplication_id();
        offers.forEach(offer -> offer.setApplicationId(id));

        return offers;
    }

    private Application createApplication(Client client) {
        Application application = new Application();
        application.setClient_id(client);
        application.setStatus(ApplicationStatus.DOCUMENT_CREATED);
        application.setCreation_date(LocalDateTime.now());

        List<StatusHistory> history = new ArrayList<>();
        StatusHistory creation = new StatusHistory();
        creation.setStatus(ApplicationStatus.DOCUMENT_CREATED);
        creation.setTime(application.getCreation_date());
        creation.setChange_type(ChangeType.AUTOMATIC);
        history.add(creation);
        application.setStatus_history_id(history);

        return application;
    }

    private void updateApplicationStatus(Application application) {
        application.setStatus(ApplicationStatus.PREAPPROVAL);

        StatusHistory update = new StatusHistory();
        update.setStatus(ApplicationStatus.PREAPPROVAL);
        update.setTime(LocalDateTime.now());
        update.setChange_type(ChangeType.AUTOMATIC);

        List<StatusHistory> history;
        if (application.getStatus_history_id() == null) history = new ArrayList<>();
        else history = application.getStatus_history_id();
        history.add(update);

        application.setStatus_history_id(history);
    }

    public void updateApplication(LoanOfferDTO loanOfferDTO) {
        Application application = applicationRepo.getByApplication_id(loanOfferDTO.getApplicationId());
        updateApplicationStatus(application);
        LoanOffer loanOffer = applicationRequestMapper.toOfferJsonb(loanOfferDTO);
        application.setApplied_offer(loanOffer);
        applicationRepo.save(application);
    }
}
