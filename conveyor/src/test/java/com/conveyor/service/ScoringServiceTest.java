package com.conveyor.service;

import com.conveyor.exception.FailedScoringException;
import com.conveyor.model.dto.CreditDTO;
import com.conveyor.model.dto.EmploymentDTO;
import com.conveyor.model.dto.ScoringDataDTO;
import com.conveyor.model.enums.EmploymentStatus;
import com.conveyor.model.enums.Gender;
import com.conveyor.model.enums.MaritalStatus;
import com.conveyor.model.enums.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ScoringServiceTest {

    private final ScoringDataDTO correctData = ScoringDataDTO.builder()
            .isInsuranceEnabled(true)
            .isSalaryClient(true)
            .amount(BigDecimal.valueOf(1000000.0))
            .term(12)
            .birthdate(LocalDate.of(1996, 11, 27))
            .employment(EmploymentDTO.builder()
                    .employmentStatus(EmploymentStatus.EMPLOYED)
                    .position(Position.WORKER)
                    .salary(BigDecimal.valueOf(100000.0))
                    .workExperienceCurrent(12)
                    .workExperienceTotal(24)
                    .build())
            .maritalStatus(MaritalStatus.SINGLE)
            .dependentAmount(1)
            .gender(Gender.MALE)
            .build();

    private final ScoringDataDTO failedScoringData = ScoringDataDTO.builder()
            .isInsuranceEnabled(true)
            .isSalaryClient(true)
            .amount(BigDecimal.valueOf(1000000000.0))
            .term(12)
            .birthdate(LocalDate.of(1790, 1, 1))
            .employment(EmploymentDTO.builder()
                    .employmentStatus(EmploymentStatus.UNEMPLOYED)
                    .position(Position.MID_MANAGER)
                    .salary(BigDecimal.valueOf(5000.0))
                    .workExperienceCurrent(0)
                    .workExperienceTotal(0)
                    .build())
            .maritalStatus(MaritalStatus.MARRIED)
            .dependentAmount(1)
            .gender(Gender.MALE)
            .build();

    @InjectMocks
    private ScoringService scoringService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(scoringService, "baseRate", BigDecimal.valueOf(18.0));
        ReflectionTestUtils.setField(scoringService, "insuranceDiscount", BigDecimal.valueOf(3.0));
        ReflectionTestUtils.setField(scoringService, "insuranceCost", BigDecimal.valueOf(100000.0));
        ReflectionTestUtils.setField(scoringService, "salaryClientDiscount", BigDecimal.valueOf(1.0));
    }

    @Test
    void failedScoring() {
        FailedScoringException exception = assertThrows(FailedScoringException.class, () -> scoringService.scoring(failedScoringData));

        assertTrue(exception.getMessage().contains("Loan amount is too big"));
        assertTrue(exception.getMessage().contains("Too old or young"));
        assertTrue(exception.getMessage().contains("Unemployment"));
        assertTrue(exception.getMessage().contains("Loan amount is too big"));
        assertTrue(exception.getMessage().contains("Not enough current work experience"));
        assertTrue(exception.getMessage().contains("Not enough work experience"));
    }

    @Test
    void successScoring() throws FailedScoringException {
        CreditDTO credit = scoringService.scoring(correctData);

        assertEquals(7.75, credit.psk().doubleValue());
        assertEquals(89787.12, credit.monthlyPayment().doubleValue());
        assertEquals(14.0, credit.rate().doubleValue());
        assertEquals(12, credit.paymentSchedule().size() - 1);
        assertEquals(0.0, credit.paymentSchedule().get(credit.term()).remainingDebt().doubleValue());
        assertEquals(0.0, credit.paymentSchedule().get(0).totalPayment().doubleValue());
    }

    @Test
    void getBaseRate() {
        Double baseRate1 = scoringService.getBaseRate(true, true).doubleValue();
        Double baseRate2 = scoringService.getBaseRate(true, false).doubleValue();
        Double baseRate3 = scoringService.getBaseRate(false, true).doubleValue();
        Double baseRate4 = scoringService.getBaseRate(false, false).doubleValue();

        assertEquals(14, baseRate1);
        assertEquals(15, baseRate2);
        assertEquals(17, baseRate3);
        assertEquals(18, baseRate4);
    }

    @Test
    void getTotalAmount() {
        Double totalAmount1 = scoringService.getTotalAmount(true, true, BigDecimal.valueOf(10000.0)).doubleValue();
        Double totalAmount2 = scoringService.getTotalAmount(false, true, BigDecimal.valueOf(10000.0)).doubleValue();
        Double totalAmount3 = scoringService.getTotalAmount(true, false, BigDecimal.valueOf(10000.0)).doubleValue();
        Double totalAmount4 = scoringService.getTotalAmount(false, false, BigDecimal.valueOf(10000.0)).doubleValue();

        assertEquals(10000.0, totalAmount1.doubleValue());
        assertEquals(10000.0, totalAmount2.doubleValue());
        assertEquals(110000.0, totalAmount3.doubleValue());
        assertEquals(10000.0, totalAmount4.doubleValue());
    }

    @Test
    void getMonthlyPayment() {
        Double monthlyPayment1 = scoringService.getMonthlyPayment(BigDecimal.valueOf(1000000.0), BigDecimal.valueOf(14.0), 24).doubleValue();
        Double monthlyPayment2 = scoringService.getMonthlyPayment(BigDecimal.valueOf(1000000.0), BigDecimal.valueOf(17.0), 24).doubleValue();
        Double monthlyPayment3 = scoringService.getMonthlyPayment(BigDecimal.valueOf(1000000.0), BigDecimal.valueOf(17.0), 100).doubleValue();

        assertEquals(48012.89, monthlyPayment1.doubleValue());
        assertEquals(49442.27, monthlyPayment2.doubleValue());
        assertEquals(18762.41, monthlyPayment3.doubleValue());
    }
}