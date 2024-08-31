package com.dossier.model.dto;

import com.dossier.model.enums.Gender;
import com.dossier.model.enums.MaritalStatus;
import com.dossier.model.json.Employment;
import com.dossier.model.json.Passport;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@Schema(title = "Client", description = "Client info.")
public class ClientDTO {

    private Long clientId;

    private String lastName;

    private String firstName;

    private String middleName;

    private LocalDate birthdate;

    private String email;

    private Gender gender;

    private MaritalStatus maritalStatus;

    private Integer dependentAmount;

    private Passport passport;

    private Employment employment;

    private String account;
}
