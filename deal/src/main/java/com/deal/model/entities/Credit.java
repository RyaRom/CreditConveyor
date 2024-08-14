package com.deal.model.entities;

import com.deal.model.enums.CreditStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long credit_id;
    private Double amount;
    private Integer term;
    private Double monthly_payment;
    private Double psk;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "credit_id")
    private List<PaymentScheduleElement> payment_schedule;
    private Boolean insurance_enable;
    private Boolean salary_client;
    private CreditStatus credit_status;
}
