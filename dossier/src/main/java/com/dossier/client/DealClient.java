package com.dossier.client;


import com.dossier.config.FeignConfig;
import com.dossier.model.dto.ApplicationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "deal-client", configuration = FeignConfig.class, url = "${feign.url}")
public interface DealClient {

    @GetMapping("/application/{applicationId}")
    ApplicationDTO getApplicationById(@PathVariable("applicationId") Long applicationId);

    @GetMapping("/application/all")
    List<ApplicationDTO> getAllApplications();

    @PutMapping("/application/{applicationId}/status")
    void updateApplicationStatusById(@PathVariable("applicationId") Long applicationId, @RequestParam("statusName") String statusName);
}

