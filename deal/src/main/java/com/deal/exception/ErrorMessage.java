package com.deal.exception;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ErrorMessage {
    private final int statusCode;
    private final Date timestamp;
    private final String message;
    private final String description;
}