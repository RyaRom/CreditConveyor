package com.deal.controller;

import com.deal.service.ApplicationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/deal/document")
@Tag(name = "Document controller")
@RequiredArgsConstructor
public class DocumentController {

    private final ApplicationService applicationService;

    @PostMapping("/{applicationId}/send")
    public ResponseEntity<Void> sendDocuments(@PathVariable Long applicationId) {
        applicationService.sendDocuments(applicationId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{applicationId}/sign")
    public ResponseEntity<Void> signDocuments(@PathVariable Long applicationId) {
        applicationService.signDocuments(applicationId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{applicationId}/code")
    public ResponseEntity<Void> verifyCode(@PathVariable Long applicationId, @RequestBody String sesCode) {
        applicationService.verifyCode(applicationId, sesCode);
        return ResponseEntity.ok().build();
    }
}

