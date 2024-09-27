package com.application;

import com.application.model.dto.LoanApplicationRequestDTO;
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
class ApplicationTests {
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

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void generateOffers() throws Exception {
		mockMvc.perform(post("/application")
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
		mockMvc.perform(post("/application")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidRequest)))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}
}
