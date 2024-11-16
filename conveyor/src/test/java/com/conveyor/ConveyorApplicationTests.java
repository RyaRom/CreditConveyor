package com.conveyor;

import com.conveyor.model.dto.EmploymentDTO;
import com.conveyor.model.dto.LoanApplicationRequestDTO;
import com.conveyor.model.dto.ScoringDataDTO;
import com.conveyor.model.enums.EmploymentStatus;
import com.conveyor.model.enums.Gender;
import com.conveyor.model.enums.MaritalStatus;
import com.conveyor.model.enums.Position;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ConveyorApplicationTests {
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

    private final ScoringDataDTO validScoringData = ScoringDataDTO.builder()
            .amount(BigDecimal.valueOf(1000000.0))
            .term(12)
            .firstName("Ivan")
            .lastName("Ivanov")
            .middleName("Ivanonovich")
            .gender(Gender.MALE)
            .birthdate(LocalDate.of(1996, 11, 27))
            .passportSeries("1234")
            .passportNumber("123456")
            .passportIssueDate(LocalDate.of(2016, 11, 27))
            .passportIssueBranch("123-456")
            .maritalStatus(MaritalStatus.SINGLE)
            .dependentAmount(1)
            .employment(EmploymentDTO.builder()
                    .employmentStatus(EmploymentStatus.EMPLOYED)
                    .employerInn("123456789012")
                    .salary(BigDecimal.valueOf(100000.0))
                    .position(Position.WORKER)
                    .workExperienceTotal(12)
                    .workExperienceCurrent(24)
                    .build())
            .account("11223344556677889900")
            .isInsuranceEnabled(true)
            .isSalaryClient(true)
            .build();

    private final ScoringDataDTO invalidScoringData = ScoringDataDTO.builder()
            .amount(BigDecimal.valueOf(0.0))
            .term(12)
            .firstName("Ivan")
            .lastName("Ivanov")
            .middleName("Ivanonovich")
            .gender(Gender.MALE)
            .birthdate(LocalDate.of(1996, 11, 27))
            .passportSeries("1234")
            .passportNumber("123456")
            .passportIssueDate(LocalDate.of(2016, 11, 27))
            .passportIssueBranch("123-456")
            .maritalStatus(MaritalStatus.SINGLE)
            .dependentAmount(1)
            .employment(EmploymentDTO.builder()
                    .employmentStatus(EmploymentStatus.UNEMPLOYED)
                    .employerInn("123456789012")
                    .salary(BigDecimal.valueOf(100000.0))
                    .position(Position.WORKER)
                    .workExperienceTotal(12)
                    .workExperienceCurrent(24)
                    .build())
            .account("11223344556677889900")
            .isInsuranceEnabled(true)
            .isSalaryClient(true)
            .build();
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void generateOffers() throws Exception {
        mockMvc.perform(post("/conveyor/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[2].totalAmount").value(1100000.0))
                .andExpect(jsonPath("$[0].rate").value(18.0));
    }

    @Test
    void invalidOffers() throws Exception {
        mockMvc.perform(post("/conveyor/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void InvalidScoringData() throws Exception {
        mockMvc.perform(post("/conveyor/calculation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidScoringData)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateCredit() throws Exception {
        mockMvc.perform(post("/conveyor/calculation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validScoringData)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(1000000.0)) // Example check on the response
                .andExpect(jsonPath("$.monthlyPayment").value(89787.12))
                .andExpect(jsonPath("$.rate").value(14.0))
                .andExpect(jsonPath("$.paymentSchedule").isNotEmpty());
    }
}
