package com.dossier.model.json;

import com.dossier.model.enums.ApplicationStatus;
import com.dossier.model.enums.ChangeType;

import java.time.LocalDateTime;

public record StatusHistory(
        ApplicationStatus status,
        LocalDateTime time,
        ChangeType changeType
) {
}

