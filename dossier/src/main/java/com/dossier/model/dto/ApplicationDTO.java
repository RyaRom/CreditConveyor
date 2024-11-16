package com.dossier.model.dto;


import com.dossier.model.enums.ApplicationStatus;
import com.dossier.model.json.LoanOffer;
import com.dossier.model.json.StatusHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;


@Schema(title = "Application", description = "Application info.")
@Builder
public record ApplicationDTO(
        Long applicationId,
        ClientDTO client,
        CreditDTO credit,
        ApplicationStatus status,
        LocalDateTime creationDate,
        LocalDateTime signDate,
        LoanOffer appliedOffer,
        String sesCode,
        List<StatusHistory> statusHistory
) {
}
