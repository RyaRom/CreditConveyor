package com.deal.model.dto;


import com.deal.model.enums.ApplicationStatus;
import com.deal.model.json.LoanOffer;
import com.deal.model.json.StatusHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Schema(title = "Application", description = "Application info.")
public record ApplicationDTO(

        Long applicationId,

        ClientDTO client,

        CreditDTO credit,

        ApplicationStatus status,

        LocalDateTime creationDate,

        LocalDateTime signDate,

        LoanOffer appliedOffer,

        String secCode,

        List<StatusHistory> statusHistory

) {
}
