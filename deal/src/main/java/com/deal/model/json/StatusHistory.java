package com.deal.model.json;

import com.deal.model.enums.ApplicationStatus;
import com.deal.model.enums.ChangeType;

import java.time.LocalDateTime;

public record StatusHistory(
        ApplicationStatus status,
        LocalDateTime time,
        ChangeType changeType
) {
}

