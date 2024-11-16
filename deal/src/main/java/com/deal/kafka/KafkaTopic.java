package com.deal.kafka;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KafkaTopic {
    SEND_SES("send-ses"),
    SEND_DOCUMENTS("send-documents"),
    APPLICATION_DENIED("application-denied"),
    CREATE_DOCUMENTS("create-documents"),
    CREDIT_ISSUED("credit-issued"),
    FINISH_REGISTRATION("finish-registration");

    private final String topicName;
}

