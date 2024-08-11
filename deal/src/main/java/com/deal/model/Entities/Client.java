package com.deal.model.Entities;

import com.deal.model.enums.Gender;
import com.deal.model.enums.MaritalStatus;
import com.deal.model.json.Employment;
import com.deal.model.json.Passport;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.time.LocalDate;

@Entity
@Getter @Setter
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long client_id;
    private String last_name;
    private String first_name;
    private String middle_name;
    private LocalDate birth_date;
    private String email;
    private Gender gender;
    private MaritalStatus marital_status;
    private Integer dependent_amount;
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Passport passport_id;
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Employment employment_id;
    private String account;
}
