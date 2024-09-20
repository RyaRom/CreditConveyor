package com.gateway.model.json;

import com.gateway.model.enums.ApplicationStatus;
import com.gateway.model.enums.ChangeType;

import java.time.LocalDateTime;

public record StatusHistory(
        ApplicationStatus status,
        LocalDateTime time,
        ChangeType changeType
) {
}

