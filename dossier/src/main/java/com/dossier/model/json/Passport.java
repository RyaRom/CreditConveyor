package com.dossier.model.json;

import java.time.LocalDate;

public record Passport(
        String series,
        String number,
        String issueBranch,
        LocalDate issueDate
) {
}

