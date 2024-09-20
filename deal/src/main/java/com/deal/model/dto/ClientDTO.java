package com.deal.model.dto;


import com.deal.model.enums.Gender;
import com.deal.model.enums.MaritalStatus;
import com.deal.model.json.Employment;
import com.deal.model.json.Passport;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(title = "Client", description = "Client info.")
public record ClientDTO(

        Long clientId,

        String lastName,

        String firstName,

        String middleName,

        LocalDate birthdate,

        String email,

        Gender gender,

        MaritalStatus maritalStatus,

        Integer dependentAmount,

        Passport passport,

        Employment employment,

        String account
) {
}
