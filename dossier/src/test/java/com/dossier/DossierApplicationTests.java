package com.dossier;

import com.dossier.client.DealClient;
import com.dossier.kafka.EmailConsumer;
import com.dossier.kafka.EmailMessage;
import com.dossier.model.dto.EmploymentDTO;
import com.dossier.model.dto.FinishRegistrationRequestDTO;
import com.dossier.model.dto.LoanApplicationRequestDTO;
import com.dossier.model.dto.LoanOfferDTO;
import com.dossier.model.enums.EmploymentStatus;
import com.dossier.model.enums.Gender;
import com.dossier.model.enums.MaritalStatus;
import com.dossier.model.enums.Position;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSendException;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.dossier.kafka.KafkaTopic.APPLICATION_DENIED;
import static com.dossier.kafka.KafkaTopic.CREATE_DOCUMENTS;
import static com.dossier.kafka.KafkaTopic.CREDIT_ISSUED;
import static com.dossier.kafka.KafkaTopic.FINISH_REGISTRATION;
import static com.dossier.kafka.KafkaTopic.SEND_DOCUMENTS;
import static com.dossier.kafka.KafkaTopic.SEND_SES;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class DossierApplicationTests {

    private static Long applicationId;

    @Autowired
    EmailConsumer emailConsumer;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void setUp(@Autowired DealClient dealClient) {
        LoanApplicationRequestDTO validRequest = LoanApplicationRequestDTO.builder()
                .amount(BigDecimal.valueOf(1000000.0))
                .term(24)
                .firstName("Ivan")
                .lastName("Ivanov")
                .middleName("Ivanonovich")
                .email("credit.conveyor.app@yandex.ru")
                .birthdate(LocalDate.of(1996, 11, 27))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();
        EmploymentDTO employment = EmploymentDTO.builder()
                .employmentStatus(EmploymentStatus.EMPLOYED)
                .employerInn("123456789012")
                .salary(BigDecimal.valueOf(100000.00))
                .position(Position.WORKER)
                .workExperienceTotal(50)
                .workExperienceCurrent(20)
                .build();
        FinishRegistrationRequestDTO finishRegistrationRequest = FinishRegistrationRequestDTO.builder()
                .gender(Gender.MALE)
                .maritalStatus(MaritalStatus.SINGLE)
                .dependentAmount(1)
                .passportIssueDate(LocalDate.of(2016, 11, 27))
                .passportIssueBranch("123-456")
                .employment(employment)
                .account("11223344556677889900")
                .build();


        List<LoanOfferDTO> offers = dealClient.generateOffers(validRequest).getBody();
        assert offers != null;
        LoanOfferDTO offer = offers.get(0);
        applicationId = offer.applicationId();

        dealClient.pickOffer(offer);
        dealClient.calculateCredit(finishRegistrationRequest, applicationId);
    }

    @AfterAll
    static void deleteMockApplication(@Autowired DealClient dealClient) {
        dealClient.deleteApplicationById(applicationId);
    }

    @Test
    void consumeFinishRegistration() throws JsonProcessingException {
        EmailMessage message = new EmailMessage("credit.conveyor.app@yandex.ru", FINISH_REGISTRATION.toString(), applicationId);
        String messageJson = objectMapper.writeValueAsString(message);
        emailConsumer.consumeFinishRegistration(messageJson);
    }

    @Test
    void consumeCreateDocuments() throws JsonProcessingException {
        EmailMessage message = new EmailMessage("credit.conveyor.app@yandex.ru", CREATE_DOCUMENTS.toString(), applicationId);
        String messageJson = objectMapper.writeValueAsString(message);
        emailConsumer.consumeCreateDocuments(messageJson);
    }

    @Test
    void consumeSendDocuments() throws JsonProcessingException {
        EmailMessage message = new EmailMessage("credit.conveyor.app@yandex.ru", SEND_DOCUMENTS.toString(), applicationId);
        String messageJson = objectMapper.writeValueAsString(message);
        emailConsumer.consumeSendDocuments(messageJson);
    }

    @Test
    void consumeSendSes() throws JsonProcessingException {
        EmailMessage message = new EmailMessage("credit.conveyor.app@yandex.ru", SEND_SES.toString(), applicationId);
        String messageJson = objectMapper.writeValueAsString(message);
        emailConsumer.consumeSendSes(messageJson);
    }

    @Test
    void consumeCreditIssued() throws JsonProcessingException {
        EmailMessage message = new EmailMessage("credit.conveyor.app@yandex.ru", CREDIT_ISSUED.toString(), applicationId);
        String messageJson = objectMapper.writeValueAsString(message);
        emailConsumer.consumeCreditIssued(messageJson);
    }

    @Test
    void consumeApplicationDenied() throws JsonProcessingException {
        EmailMessage message = new EmailMessage("credit.conveyor.app@yandex.ru", APPLICATION_DENIED.toString(), applicationId);
        String messageJson = objectMapper.writeValueAsString(message);
        emailConsumer.consumeApplicationDenied(messageJson);
    }

    @Test
    void wrongMail() throws JsonProcessingException {
        EmailMessage message = new EmailMessage("credit.conveyor.app", APPLICATION_DENIED.toString(), applicationId);
        String messageJson = objectMapper.writeValueAsString(message);
        assertThrows(MailSendException.class, () -> emailConsumer.consumeSendDocuments(messageJson));
    }

    @Test
    void noApplication() throws JsonProcessingException {
        EmailMessage message = new EmailMessage("credit.conveyor.app@yandex.ru", APPLICATION_DENIED.toString(), -1L);
        String messageJson = objectMapper.writeValueAsString(message);
        assertThrows(ResponseStatusException.class, () -> emailConsumer.consumeSendDocuments(messageJson));
    }
}
