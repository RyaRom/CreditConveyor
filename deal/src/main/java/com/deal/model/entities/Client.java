package com.deal.model.entities;

import com.deal.model.enums.Gender;
import com.deal.model.enums.MaritalStatus;
import com.deal.model.json.Employment;
import com.deal.model.json.Passport;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;
    private String lastName;
    private String firstName;
    private String middleName;
    private LocalDate birthdate;
    private String email;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;
    private Integer dependentAmount;
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Passport passportId;
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Employment employmentId;
    private String account;
}
