package com.deal.controller;

import com.deal.model.dto.ApplicationDTO;
import com.deal.service.ApplicationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Admin controller")
public class AdminController {

    private final ApplicationService applicationService;

    @GetMapping("/application/{applicationId}")
    public ApplicationDTO getApplicationById(@PathVariable Long applicationId) {
        return applicationService.getApplicationDtoById(applicationId);
    }

    @GetMapping("/application/all")
    public List<ApplicationDTO> getAllApplications() {
        return applicationService.getAllApplications();
    }

    @PutMapping("/application/{applicationId}/status")
    public void updateApplicationStatusById(@PathVariable Long applicationId, @RequestParam String statusName) {
        applicationService.updateApplicationStatusById(applicationId, statusName);
    }

    @DeleteMapping("/application/{applicationId}/delete")
    public void deleteApplicationById(@PathVariable Long applicationId) {
        applicationService.deleteApplicationById(applicationId);
    }
}
