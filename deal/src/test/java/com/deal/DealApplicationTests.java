package com.deal;

import com.deal.model.dto.EmploymentDTO;
import com.deal.model.dto.FinishRegistrationRequestDTO;
import com.deal.model.dto.LoanApplicationRequestDTO;
import com.deal.model.dto.LoanOfferDTO;
import com.deal.model.entities.Application;
import com.deal.model.entities.Client;
import com.deal.model.entities.Credit;
import com.deal.model.enums.ApplicationStatus;
import com.deal.model.enums.EmploymentStatus;
import com.deal.model.enums.Gender;
import com.deal.model.enums.MaritalStatus;
import com.deal.model.enums.Position;
import com.deal.model.mapping.ApplicationRequestMapper;
import com.deal.repo.ApplicationRepo;
import com.deal.repo.ClientRepo;
import com.deal.repo.CreditRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class DealApplicationTests {

    private final LoanApplicationRequestDTO validRequest = LoanApplicationRequestDTO.builder()
            .amount(BigDecimal.valueOf(1000000.0))
            .term(24)
            .firstName("Ivan")
            .lastName("Ivanov")
            .middleName("Ivanonovich")
            .email("iivanov@email.ru")
            .birthdate(LocalDate.of(1996, 11, 27))
            .passportSeries("1234")
            .passportNumber("123456")
            .build();

    private final EmploymentDTO employment = EmploymentDTO.builder()
            .employmentStatus(EmploymentStatus.EMPLOYED)
            .employerInn("123456789012")
            .salary(BigDecimal.valueOf(100000.00))
            .position(Position.WORKER)
            .workExperienceTotal(50)
            .workExperienceCurrent(20)
            .build();

    private final EmploymentDTO badEmployment = EmploymentDTO.builder()
            .employmentStatus(EmploymentStatus.UNEMPLOYED)
            .employerInn("123456789012")
            .salary(BigDecimal.valueOf(0.00))
            .build();

    private final FinishRegistrationRequestDTO finishRegistrationRequest = FinishRegistrationRequestDTO.builder()
            .gender(Gender.MALE)
            .maritalStatus(MaritalStatus.SINGLE)
            .dependentAmount(1)
            .passportIssueDate(LocalDate.of(2016, 11, 27))
            .passportIssueBranch("123-456")
            .employment(employment)
            .account("11223344556677889900")
            .build();

    private final FinishRegistrationRequestDTO finishRegistrationRequestDenied = FinishRegistrationRequestDTO.builder()
            .gender(Gender.MALE)
            .maritalStatus(MaritalStatus.SINGLE)
            .dependentAmount(1)
            .passportIssueDate(LocalDate.of(2016, 11, 27))
            .passportIssueBranch("123-456")
            .employment(badEmployment)
            .account("11223344556677889900")
            .build();


    private final LoanApplicationRequestDTO invalidRequest = LoanApplicationRequestDTO.builder()
            .amount(BigDecimal.valueOf(-200.0))
            .term(24)
            .firstName("Ivan")
            .lastName("Ivanov")
            .middleName("Ivanonovich")
            .email("iivanov@email.ru")
            .birthdate(LocalDate.of(1996, 11, 27))
            .passportSeries("1234")
            .passportNumber("")
            .build();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationRequestMapper applicationRequestMapper;

    @Autowired
    private ApplicationRepo applicationRepo;

    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private CreditRepo creditRepo;

    private static boolean areAllFieldsInjected(Object object, Set<String> nullFields) {
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (nullFields.contains(field.getName())) continue;
            try {
                if (field.get(object) == null) return false;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    @Test
    void creditScoringDenied() throws Exception {
        String result = mockMvc.perform(post("/deal/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<LoanOfferDTO> offers = objectMapper.readValue(result, new TypeReference<>() {
        });
        LoanOfferDTO offer = offers.get(0);
        Long applicationId = offer.getApplicationId();

        mockMvc.perform(put("/deal/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offer)))
                .andExpect(status().isNoContent());

        mockMvc.perform(put("/deal/calculate/" + applicationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(finishRegistrationRequestDenied)))
                .andExpect(status().isNoContent());
        Application application = applicationRepo.getByApplicationId(applicationId);

        assertNull(application.getCreditId());
        assertEquals(application.getStatus(), ApplicationStatus.CC_DENIED);
        assertEquals(application.getStatusHistoryId().size(), 3);
    }

    @Test
    void creditCreationChain() throws Exception {
        String result = mockMvc.perform(post("/deal/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[2].totalAmount").value(1100000.0))
                .andExpect(jsonPath("$[0].rate").value(18.0))
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<LoanOfferDTO> offers = objectMapper.readValue(result, new TypeReference<>() {
        });
        LoanOfferDTO offer = offers.get(0);
        Long applicationId = offer.getApplicationId();

        mockMvc.perform(put("/deal/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offer)))
                .andExpect(status().isNoContent());

        Application application = applicationRepo.getByApplicationId(applicationId);

        assertEquals(application.getStatus(), ApplicationStatus.APPROVED);
        assertEquals(application.getStatusHistoryId().size(), 2);
        assertEquals(application.getAppliedOffer().toString(), applicationRequestMapper.toOfferJsonb(offer).toString());

        mockMvc.perform(put("/deal/calculate/" + applicationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(finishRegistrationRequest)))
                .andExpect(status().isNoContent());
        Credit credit = creditRepo.getByCreditId(application.getCreditId().getCreditId());
        Client client = clientRepo.getByClientId(application.getClientId().getClientId());

        application = applicationRepo.getByApplicationId(applicationId);
        assertNotNull(credit);
        assertNotNull(client);
        assertEquals(application.getStatus(), ApplicationStatus.CC_APPROVED);
        assertEquals(application.getStatusHistoryId().size(), 3);
        assertEquals(client.getEmploymentId().toString(), applicationRequestMapper.toEmploymentJsonb(employment).toString());
        assertEquals(client.getGender(), finishRegistrationRequest.getGender());
        assertTrue(areAllFieldsInjected(credit, Collections.emptySet()));
        assertTrue(areAllFieldsInjected(client, Collections.emptySet()));
        assertTrue(areAllFieldsInjected(application, Set.of("secCode", "signDate")));
    }

    @Test
    void createOffers() throws Exception {
        String result = mockMvc.perform(post("/deal/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[2].totalAmount").value(1100000.0))
                .andExpect(jsonPath("$[0].rate").value(18.0))
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<LoanOfferDTO> offers = objectMapper.readValue(result, new TypeReference<>() {
        });
        System.out.printf("\n\n offers: %s\n\n", offers.stream().map(LoanOfferDTO::toString).collect(Collectors.joining("\n")));
        Long applicationId = offers.get(0).getApplicationId();
        Application application = applicationRepo.getByApplicationId(applicationId);
        Client client = clientRepo.getByClientId(application.getClientId().getClientId());

        assertNotNull(application);
        assertNotNull(client);
        assertEquals(application.getStatus(), ApplicationStatus.PREAPPROVAL);
        assertEquals(application.getStatusHistoryId().size(), 1);
        assertEquals(client.getPassportId().series(), validRequest.getPassportSeries());
        assertEquals(client.getFirstName(), validRequest.getFirstName());
        assertEquals(client.getMiddleName(), validRequest.getMiddleName());
        assertEquals(client.getLastName(), validRequest.getLastName());
        assertEquals(client.getBirthdate(), validRequest.getBirthdate());
    }

    @Test
    void invalidOffers() throws Exception {
        mockMvc.perform(post("/deal/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }
}
