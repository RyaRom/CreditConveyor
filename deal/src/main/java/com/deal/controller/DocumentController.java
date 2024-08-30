package com.deal.controller;

import com.deal.service.ApplicationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/deal/document")
@Tag(name = "Document conveyor")
@RequiredArgsConstructor
public class DocumentController {

    private final ApplicationService applicationService;

    @PostMapping("/{applicationId}/send")
    public void sendDocuments(@PathVariable Long applicationId) {
        applicationService.sendDocuments(applicationId);
    }

    @PostMapping("/{applicationId}/sign")
    public void signDocuments(@PathVariable Long applicationId) {
    }

    @PostMapping("/{applicationId}/code")
    public void verifyCode(@PathVariable Long applicationId, @RequestBody Integer sesCode) {
    }
}

