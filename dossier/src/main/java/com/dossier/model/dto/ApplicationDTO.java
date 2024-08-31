package com.dossier.model.dto;


import com.dossier.model.enums.ApplicationStatus;
import com.dossier.model.json.LoanOffer;
import com.dossier.model.json.StatusHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Schema(title = "Application", description = "Application info.")
public class ApplicationDTO {

    private Long applicationId;

    private ClientDTO client;

    private CreditDTO credit;

    private ApplicationStatus status;

    private LocalDateTime creationDate;

    private LocalDateTime signDate;

    private LoanOffer appliedOffer;

    private String secCode;

    private List<StatusHistory> statusHistory;

}
