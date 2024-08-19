package com.application.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        HttpStatus status = HttpStatus.valueOf(response.status());
        return switch (status) {
            case BAD_REQUEST ->
                    new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request or scoring error: " + response.request().url());
            case INTERNAL_SERVER_ERROR ->
                    new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error: " + response.request().url());
            default -> new Exception("Unexpected error: " + response.status() + " at " + response.request().url());
        };
    }
}
