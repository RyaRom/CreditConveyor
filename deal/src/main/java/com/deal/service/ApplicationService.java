package com.deal.service;

import com.deal.client.ConveyorClient;
import com.deal.kafka.EmailProducer;
import com.deal.kafka.KafkaTopic;
import com.deal.model.dto.*;
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
import com.deal.model.mapping.DataMapper;
import com.deal.repo.ApplicationRepo;
import com.deal.repo.ClientRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationService {

    private final ConveyorClient conveyorClient;

    private final DataMapper dataMapper;

    private final ClientRepo clientRepo;

    private final ApplicationRepo applicationRepo;

    private final EmailProducer emailProducer;

    private String generateSESCode() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[20];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().encodeToString(randomBytes);
    }

    @Transactional
    public List<LoanOfferDTO> generateOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        Client client = dataMapper.toClient(loanApplicationRequestDTO);
        Application application = createApplication(client);

        clientRepo.save(client);
        applicationRepo.save(application);

        List<LoanOfferDTO> offers = conveyorClient.createOffers(loanApplicationRequestDTO);
        log.info("Response from conveyor MC: {}", offers);
        Long id = application.getApplicationId();
        for (int i = 0, offersSize = offers.size(); i < offersSize; i++) {
            LoanOfferDTO offer = offers.get(i);
            offers.set(i, LoanOfferDTO
                    .builder()
                    .applicationId(id)
                    .requestedAmount(offer.requestedAmount())
                    .totalAmount(offer.totalAmount())
                    .term(offer.term())
                    .monthlyPayment(offer.monthlyPayment())
                    .rate(offer.rate())
                    .isInsuranceEnabled(offer.isInsuranceEnabled())
                    .isSalaryClient(offer.isSalaryClient())
                    .build());
        }

        return offers;
    }

    private Application createApplication(Client client) {
        Application application = new Application();
        application.setClientId(client);
        application.setCreationDate(LocalDateTime.now());
        updateApplicationStatus(application, ApplicationStatus.PREAPPROVAL);

        return application;
    }

    @Transactional
    public void updateApplicationStatusById(Long applicationId, String status) {
        Application application = getApplicationById(applicationId);
        log.info("Update application {} to status {}", applicationId, status);

        if (status.equals(ApplicationStatus.CLIENT_DENIED.toString())) {
            log.info("Client denied for application {}", applicationId);
            emailProducer.sendMessage(application.getClientId().getEmail(), KafkaTopic.APPLICATION_DENIED, applicationId);
            updateApplicationStatus(application, ApplicationStatus.CLIENT_DENIED);
            applicationRepo.save(application);
            return;
        }

        updateApplicationStatus(application, ApplicationStatus.DOCUMENT_CREATED);
        applicationRepo.save(application);
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

    @Transactional
    public void updateApplication(LoanOfferDTO loanOfferDTO) {
        Application application = applicationRepo.getByApplicationId(loanOfferDTO.applicationId())
                .orElseThrow(() -> new EntityNotFoundException("Application not found for id: " + loanOfferDTO.applicationId()));
        updateApplicationStatus(application, ApplicationStatus.APPROVED);
        LoanOffer loanOffer = dataMapper.toOfferJsonb(loanOfferDTO);
        application.setAppliedOffer(loanOffer);
        applicationRepo.save(application);
        emailProducer.sendMessage(application.getClientId().getEmail(), KafkaTopic.FINISH_REGISTRATION, application.getApplicationId());
    }

    @Transactional
    protected void fillDataFromRegistrationRequest(FinishRegistrationRequestDTO request, Application application) {
        Client client = application.getClientId();
        Passport passport = new Passport(
                client.getPassportId().series(),
                client.getPassportId().number(),
                request.passportIssueBranch(),
                request.passportIssueDate()
        );

        client.setGender(request.gender());
        client.setMaritalStatus(request.maritalStatus());
        client.setDependentAmount(request.dependentAmount());
        client.setPassportId(passport);
        client.setAccount(request.account());

        Employment employment = dataMapper.toEmploymentJsonb(request.employment());
        client.setEmploymentId(employment);

        clientRepo.save(client);
    }

    @Transactional
    public void applicationScoring(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId) {
        Application application = getApplicationById(applicationId);
        ScoringDataDTO scoringData = dataMapper.mapScoringData(finishRegistrationRequestDTO, application);
        log.info("Scoring data: {}", scoringData.toString());
        fillDataFromRegistrationRequest(finishRegistrationRequestDTO, application);

        CreditDTO creditDTO;
        try {
            creditDTO = conveyorClient.calculateCredit(scoringData);
        } catch (ResponseStatusException e) {
            log.info("Failed scoring: {}", applicationId);
            updateApplicationStatus(application, ApplicationStatus.CC_DENIED);
            applicationRepo.save(application);
            emailProducer.sendMessage(application.getClientId().getEmail(), KafkaTopic.APPLICATION_DENIED, applicationId);
            return;
        }

        log.info("Response from conveyor: credit {}", creditDTO.toString());
        Credit credit = dataMapper.toCredit(creditDTO);
        credit.setCreditStatus(CreditStatus.CALCULATED);
        application.setCreditId(credit);

        updateApplicationStatus(application, ApplicationStatus.CC_APPROVED);
        applicationRepo.save(application);
        emailProducer.sendMessage(application.getClientId().getEmail(), KafkaTopic.CREATE_DOCUMENTS, applicationId);
    }

    @Transactional
    public void sendDocuments(Long applicationId) {
        Application application = getApplicationById(applicationId);
        updateApplicationStatus(application, ApplicationStatus.PREPARE_DOCUMENTS);
        applicationRepo.save(application);
        emailProducer.sendMessage(application.getClientId().getEmail(), KafkaTopic.SEND_DOCUMENTS, applicationId);
    }

    @Transactional
    public void signDocuments(Long applicationId) {
        Application application = getApplicationById(applicationId);
        String sesCode = generateSESCode();
        application.setSecCode(sesCode);
        log.info("Generated ses code {}\n For application {}", sesCode, applicationId);

        applicationRepo.save(application);
        emailProducer.sendMessage(application.getClientId().getEmail(), KafkaTopic.SEND_SES, applicationId);
    }

    @Transactional
    @SneakyThrows
    public void verifyCode(Long applicationId, String sesCode) {
        Application application = getApplicationById(applicationId);

        if (!application.getSecCode().equals(sesCode)) {
            throw new AccessDeniedException("Ses code %s is wrong for application Id: %s".formatted(sesCode, applicationId));
        }

        updateApplicationStatus(application, ApplicationStatus.DOCUMENT_SIGNED);
        application.getCreditId().setCreditStatus(CreditStatus.ISSUED);
        updateApplicationStatus(application, ApplicationStatus.CREDIT_ISSUED);
        application.setSignDate(LocalDateTime.now());

        applicationRepo.save(application);

        emailProducer.sendMessage(application.getClientId().getEmail(), KafkaTopic.CREDIT_ISSUED, applicationId);
    }

    @Transactional
    protected Application getApplicationById(Long applicationId) {
        return applicationRepo.getByApplicationId(applicationId).orElseThrow(() -> new EntityNotFoundException("Application not found for id: " + applicationId));
    }

    @Transactional
    public ApplicationDTO getApplicationDtoById(Long applicationId) {
        Application application = getApplicationById(applicationId);
        return dataMapper.toApplicationDTO(application);
    }

    @Transactional
    public List<ApplicationDTO> getAllApplications() {
        List<Application> applications = applicationRepo.findAll();
        return applications.stream().map(dataMapper::toApplicationDTO).toList();
    }

    @Transactional
    public void deleteApplicationById(Long applicationId) {
        applicationRepo.deleteById(applicationId);
    }
}
