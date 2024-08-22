package com.deal.model.entities;

import com.deal.model.enums.ApplicationStatus;
import com.deal.model.json.LoanOffer;
import com.deal.model.json.StatusHistory;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "client_id")
    private Client clientId;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "credit_id")
    private Credit creditId;
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
    private LocalDateTime creationDate;
    private LocalDateTime signDate;
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private LoanOffer appliedOffer;
    private String secCode;
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<StatusHistory> statusHistoryId;
}
