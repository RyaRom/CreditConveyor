package com.deal.model.json;

import com.deal.model.enums.ChangeType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StatusHistory {
    private String status;
    private LocalDateTime time;
    private ChangeType change_type;
}
