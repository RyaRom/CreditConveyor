package com.deal.model.Entities;

import com.deal.model.enums.ApplicationStatus;
import com.deal.model.json.StatusHistory;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;


import java.time.LocalDateTime;

@Entity
@Getter @Setter
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long application_id;
    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client_id;
    @OneToOne
    @JoinColumn(name = "credit_id")
    private Credit credit_id;
    private ApplicationStatus status;
    private LocalDateTime creation_date;
    private LocalDateTime sign_date;
    private String sec_code;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private StatusHistory status_history_id;
}
