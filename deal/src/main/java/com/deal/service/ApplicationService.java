package com.deal.service;

import com.deal.client.ConveyorClient;
import com.deal.kafka.EmailProducer;
import com.deal.kafka.KafkaTopic;
import com.deal.model.dto.CreditDTO;
import com.deal.model.dto.FinishRegistrationRequestDTO;
import com.deal.model.dto.LoanApplicationRequestDTO;
import com.deal.model.dto.LoanOfferDTO;
import com.deal.model.dto.ScoringDataDTO;
import com.deal.model.entities.Application;
import com.deal.model.entities.Client;
import com.deal.model.entities.Credit;
import com.deal.model.enums.ApplicationStatus;
import com.deal.model.enums.ChangeType;
import com.deal.model.enums.CreditStatus;
import com.deal.model.json.Employment;
import com.deal.model.json.LoanOffer;
import com.deal.model.json.Passport;
import com.deal.model.json.StatusHistory;
import com.deal.model.mapping.ApplicationRequestMapper;
import com.deal.repo.ApplicationRepo;
import com.deal.repo.ClientRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ApplicationService {

    private final ConveyorClient conveyorClient;

    private final ApplicationRequestMapper applicationRequestMapper;

    private final ClientRepo clientRepo;

    private final ApplicationRepo applicationRepo;

    private final EmailProducer emailProducer;

    public List<LoanOfferDTO> generateOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        Client client = applicationRequestMapper.toClient(loanApplicationRequestDTO);
        Application application = createApplication(client);

        clientRepo.save(client);
        applicationRepo.save(application);

        List<LoanOfferDTO> offers = conveyorClient.createOffers(loanApplicationRequestDTO);
        log.info("Response from conveyor MC: {}", offers);
        Long id = application.getApplicationId();
        offers.forEach(offer -> offer.setApplicationId(id));

        return offers;
    }

    private Application createApplication(Client client) {
        Application application = new Application();
        application.setClientId(client);
        application.setCreationDate(LocalDateTime.now());
        updateApplicationStatus(application, ApplicationStatus.PREAPPROVAL);

        return application;
    }

    private void updateApplicationStatus(Application application, ApplicationStatus status) {
        application.setStatus(status);

        StatusHistory update = new StatusHistory(status, LocalDateTime.now(), ChangeType.MANUAL);

        List<StatusHistory> history;
        if (application.getStatusHistoryId() == null) {
            history = new ArrayList<>();
        } else {
            history = application.getStatusHistoryId();
        }
        history.add(update);

        log.info("Updated status to {}. Application id: {}", status, application.getApplicationId());
        application.setStatusHistoryId(history);
    }

    public void updateApplication(LoanOfferDTO loanOfferDTO) {
        Application application = applicationRepo.getByApplicationId(loanOfferDTO.getApplicationId()).orElseThrow();
        updateApplicationStatus(application, ApplicationStatus.APPROVED);
        LoanOffer loanOffer = applicationRequestMapper.toOfferJsonb(loanOfferDTO);
        application.setAppliedOffer(loanOffer);
        applicationRepo.save(application);
        emailProducer.sendMessage(application.getClientId().getEmail(), KafkaTopic.FINISH_REGISTRATION, application.getApplicationId());
    }

    protected void fillDataFromRegistrationRequest(FinishRegistrationRequestDTO request, Application application) {
        Client client = application.getClientId();
        Passport passport = new Passport(
                client.getPassportId().series(),
                client.getPassportId().number(),
                request.getPassportIssueBranch(),
                request.getPassportIssueDate()
        );

        client.setGender(request.getGender());
        client.setMaritalStatus(request.getMaritalStatus());
        client.setDependentAmount(request.getDependentAmount());
        client.setPassportId(passport);
        client.setAccount(request.getAccount());

        Employment employment = applicationRequestMapper.toEmploymentJsonb(request.getEmployment());
        client.setEmploymentId(employment);

        clientRepo.save(client);
    }

    public void applicationScoring(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId) {
        Application application = applicationRepo.getByApplicationId(applicationId).orElseThrow();
        ScoringDataDTO scoringData = applicationRequestMapper.mapScoringData(finishRegistrationRequestDTO, application);
        log.info("Scoring data: {}", scoringData.toString());
        fillDataFromRegistrationRequest(finishRegistrationRequestDTO, application);

        CreditDTO creditDTO;
        try {
            creditDTO = conveyorClient.calculateCredit(scoringData);
        } catch (ResponseStatusException e) {
            log.info("Failed scoring: {}", applicationId);
            updateApplicationStatus(application, ApplicationStatus.CC_DENIED);
            applicationRepo.save(application);
            return;
        }

        log.info("Response from conveyor: credit {}", creditDTO.toString());
        Credit credit = applicationRequestMapper.toCredit(creditDTO);
        credit.setCreditStatus(CreditStatus.CALCULATED);
        application.setCreditId(credit);

        updateApplicationStatus(application, ApplicationStatus.CC_APPROVED);
        applicationRepo.save(application);
        emailProducer.sendMessage(application.getClientId().getEmail(), KafkaTopic.CREATE_DOCUMENTS, applicationId);
    }

    public void sendDocuments(Long applicationId) {
        Application application = applicationRepo.getByApplicationId(applicationId).orElseThrow();
        updateApplicationStatus(application, ApplicationStatus.PREPARE_DOCUMENTS);
        applicationRepo.save(application);
        emailProducer.sendMessage(application.getClientId().getEmail(), KafkaTopic.SEND_DOCUMENTS, applicationId);
    }
}
