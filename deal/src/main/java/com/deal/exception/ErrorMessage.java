package com.deal.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorMessage {
    private final int statusCode;
    private final LocalDateTime timestamp;
    private final String message;
    private final String description;
}