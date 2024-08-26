package com.deal.model.json;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class Passport {
    private String series;
    private String number;
    private String issueBranch;
    private LocalDate issueDate;
}
