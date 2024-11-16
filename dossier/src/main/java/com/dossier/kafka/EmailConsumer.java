package com.dossier.kafka;

import com.dossier.client.DealClient;
import com.dossier.model.enums.ApplicationStatus;
import com.dossier.service.MailSenderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailConsumer {

    private final ObjectMapper objectMapper;

    private final MailSenderService mailSenderService;

    private final DealClient dealClient;


    @KafkaListener(topics = "application-denied", groupId = "CreditConveyor")
    public void consumeApplicationDenied(String message) throws JsonProcessingException {
        log.info("Received application-denied message: {}", message);
        EmailMessage emailMessage = objectMapper.readValue(message, EmailMessage.class);
        mailSenderService.sendEmailByTheme(emailMessage, KafkaTopic.APPLICATION_DENIED);
    }

    @KafkaListener(topics = "create-documents", groupId = "CreditConveyor")
    public void consumeCreateDocuments(String message) throws JsonProcessingException {
        log.info("Received create-documents message: {}", message);
        EmailMessage emailMessage = objectMapper.readValue(message, EmailMessage.class);
        mailSenderService.sendEmailByTheme(emailMessage, KafkaTopic.CREATE_DOCUMENTS);
    }

    @KafkaListener(topics = "credit-issued", groupId = "CreditConveyor")
    public void consumeCreditIssued(String message) throws JsonProcessingException {
        log.info("Received credit-issued message: {}", message);
        EmailMessage emailMessage = objectMapper.readValue(message, EmailMessage.class);
        mailSenderService.sendEmailByTheme(emailMessage, KafkaTopic.CREDIT_ISSUED);
    }

    @KafkaListener(topics = "finish-registration", groupId = "CreditConveyor")
    public void consumeFinishRegistration(String message) throws JsonProcessingException {
        log.info("Received finish-registration message: {}", message);
        EmailMessage emailMessage = objectMapper.readValue(message, EmailMessage.class);
        mailSenderService.sendEmailByTheme(emailMessage, KafkaTopic.FINISH_REGISTRATION);
    }

    @KafkaListener(topics = "send-documents", groupId = "CreditConveyor")
    public void consumeSendDocuments(String message) throws JsonProcessingException {
        log.info("Received send-documents message: {}", message);
        EmailMessage emailMessage = objectMapper.readValue(message, EmailMessage.class);
        mailSenderService.sendEmailByTheme(emailMessage, KafkaTopic.SEND_DOCUMENTS);
        dealClient.updateApplicationStatusById(emailMessage.applicationId(), ApplicationStatus.DOCUMENT_CREATED.toString());
    }

    @KafkaListener(topics = "send-ses", groupId = "CreditConveyor")
    public void consumeSendSes(String message) throws JsonProcessingException {
        log.info("Received send-ses message: {}", message);
        EmailMessage emailMessage = objectMapper.readValue(message, EmailMessage.class);
        mailSenderService.sendEmailByTheme(emailMessage, KafkaTopic.SEND_SES);
    }
}

