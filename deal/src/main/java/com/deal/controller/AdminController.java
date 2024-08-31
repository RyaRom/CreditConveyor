package com.deal.controller;

import com.deal.service.ApplicationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Admin controller")
public class AdminController {

    private final ApplicationService applicationService;

    @PutMapping("/application/{applicationId}/status")
    public void updateApplicationStatusById(@PathVariable Long applicationId, @RequestParam String statusName) {
        applicationService.updateApplicationStatusById(applicationId, statusName);
    }
}
