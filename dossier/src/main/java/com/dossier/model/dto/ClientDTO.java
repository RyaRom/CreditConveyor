package com.dossier.model.dto;

import com.dossier.model.enums.Gender;
import com.dossier.model.enums.MaritalStatus;
import com.dossier.model.json.Employment;
import com.dossier.model.json.Passport;
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