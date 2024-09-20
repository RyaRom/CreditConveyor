package com.gateway.feign;

import com.gateway.model.dto.FinishRegistrationRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "deal-client", configuration = FeignConfig.class, url = "${feign.deal-url")
public interface DealClient {
    @PutMapping("/calculate/{applicationId}")
    ResponseEntity<Void> calculateCredit(FinishRegistrationRequestDTO finishRegistrationRequestDTO, @PathVariable Long applicationId);

    @PostMapping("/deal/document/{applicationId}/send")
    ResponseEntity<Void> sendDocuments(@PathVariable Long applicationId);

    @PostMapping("/deal/document/{applicationId}/sign")
    ResponseEntity<Void> signDocuments(@PathVariable Long applicationId);

    @PostMapping("/deal/document/{applicationId}/code")
    ResponseEntity<Void> verifyCode(@PathVariable Long applicationId, String sesCode);
}