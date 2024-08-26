package com.deal.model.json;

import com.deal.model.enums.ApplicationStatus;
import com.deal.model.enums.ChangeType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class StatusHistory {
    private ApplicationStatus status;
    private LocalDateTime time;
    private ChangeType changeType;
}
