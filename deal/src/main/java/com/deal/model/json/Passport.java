package com.deal.model.json;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Passport {
    private String series;
    private String number;
    private String issue_branch;
    private LocalDate issue_date;
}
