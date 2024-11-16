package com.gateway.feign;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public ErrorDecoder conveyorErrorDecoder() {
        return new FeignErrorDecoder();
    }
}
