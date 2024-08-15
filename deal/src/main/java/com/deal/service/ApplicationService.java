package com.deal.service;

import com.deal.config.ConveyorClient;
import com.deal.model.dto.*;
import com.deal.model.entities.Application;
import com.deal.model.entities.Client;
import com.deal.model.entities.Credit;
import com.deal.model.enums.ApplicationStatus;
import com.deal.model.enums.ChangeType;
import com.deal.model.enums.CreditStatus;
import com.deal.model.json.Employment;
import com.deal.model.json.LoanOffer;
import com.deal.model.json.StatusHistory;
import com.deal.model.mapping.ApplicationRequestMapper;
import com.deal.repo.ApplicationRepo;
import com.deal.repo.ClientRepo;
import com.deal.repo.CreditRepo;
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
public class ApplicationService {
    private final ConveyorClient conveyorClient;
    private final ApplicationRequestMapper applicationRequestMapper;
    private final ClientRepo clientRepo;
    private final CreditRepo creditRepo;
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
        application.setCreation_date(LocalDateTime.now());
        updateApplicationStatus(application, ApplicationStatus.DOCUMENT_CREATED);

        return application;
    }

    private void updateApplicationStatus(Application application, ApplicationStatus status) {
        application.setStatus(status);

        StatusHistory update = new StatusHistory();
        update.setStatus(status);
        update.setTime(LocalDateTime.now());
        update.setChange_type(ChangeType.AUTOMATIC);

        List<StatusHistory> history;
        if (application.getStatus_history_id() == null) history = new ArrayList<>();
        else history = application.getStatus_history_id();
        history.add(update);

        log.info("Updated status to {}: {}", status, application.getApplication_id());
        application.setStatus_history_id(history);
    }

    public void updateApplication(LoanOfferDTO loanOfferDTO) {
        Application application = applicationRepo.getByApplication_id(loanOfferDTO.getApplicationId());
        updateApplicationStatus(application, ApplicationStatus.PREAPPROVAL);
        LoanOffer loanOffer = applicationRequestMapper.toOfferJsonb(loanOfferDTO);
        application.setApplied_offer(loanOffer);
        applicationRepo.save(application);
    }

    private ScoringDataDTO mapScoringData(FinishRegistrationRequestDTO request, Application application) {
        LoanOffer offer = application.getApplied_offer();
        Client client = application.getClient_id();
        return ScoringDataDTO.builder()
                .amount(offer.getRequestedAmount())
                .term(offer.getTerm())
                .firstName(client.getFirst_name())
                .middleName(client.getMiddle_name())
                .lastName(client.getLast_name())
                .gender(request.getGender())
                .birthdate(client.getBirth_date())
                .passportSeries(client.getPassport_id().getSeries())
                .passportNumber(client.getPassport_id().getNumber())
                .passportIssueBranch(request.getPassportIssueBranch())
                .passportIssueDate(request.getPassportIssueDate())
                .maritalStatus(request.getMaritalStatus())
                .dependentAmount(request.getDependentAmount())
                .employment(request.getEmployment())
                .account(request.getAccount())
                .isInsuranceEnabled(offer.getIsInsuranceEnabled())
                .isSalaryClient(offer.getIsSalaryClient())
                .build();
    }

    private void fillDataFromRegistrationRequest(FinishRegistrationRequestDTO request, Application application) {
        Client client = application.getClient_id();
        client.setGender(request.getGender());
        client.setMarital_status(request.getMaritalStatus());
        client.setDependent_amount(request.getDependentAmount());
        client.getPassport_id().setIssue_date(request.getPassportIssueDate());
        client.getPassport_id().setIssue_branch(request.getPassportIssueBranch());
        client.setAccount(request.getAccount());

        Employment employment = applicationRequestMapper.toEmploymentJsonb(request.getEmployment());
        client.setEmployment_id(employment);

        clientRepo.save(client);
    }

    public void applicationScoring(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId) {
        Application application = applicationRepo.getByApplication_id(applicationId);
        ScoringDataDTO scoringData = mapScoringData(finishRegistrationRequestDTO, application);
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
        credit.setCredit_status(CreditStatus.CALCULATED);
        application.setCredit_id(credit);

        updateApplicationStatus(application, ApplicationStatus.CC_APPROVED);
        applicationRepo.save(application);
        creditRepo.save(credit);
    }
}