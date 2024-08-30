package com.deal.kafka;

import jakarta.validation.constraints.Pattern;

public record EmailMessage(
        @Pattern(regexp = "[\\w.]*@\\w{2,10}\\.\\w{2,5}")
        String address,
        String theme,
        Long applicationId
) {

}