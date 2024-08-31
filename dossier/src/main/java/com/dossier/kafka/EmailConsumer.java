package com.dossier.kafka;

import com.dossier.service.MailSenderService;
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


    @KafkaListener(topics = "application-denied", groupId = "CreditConveyor")
    public void consumeApplicationDenied(String message) {
        log.info("Received application-denied message: {}", message);
        EmailMessage emailMessage = objectMapper.convertValue(message, EmailMessage.class);
        mailSenderService.sendEmailByTheme(emailMessage, KafkaTopic.APPLICATION_DENIED);
    }

    @KafkaListener(topics = "create-documents", groupId = "CreditConveyor")
    public void consumeCreateDocuments(String message) {
        log.info("Received create-documents message: {}", message);
        EmailMessage emailMessage = objectMapper.convertValue(message, EmailMessage.class);
        mailSenderService.sendEmailByTheme(emailMessage, KafkaTopic.CREATE_DOCUMENTS);
    }

    @KafkaListener(topics = "credit-issued", groupId = "CreditConveyor")
    public void consumeCreditIssued(String message) {
        log.info("Received credit-issued message: {}", message);
        EmailMessage emailMessage = objectMapper.convertValue(message, EmailMessage.class);
        mailSenderService.sendEmailByTheme(emailMessage, KafkaTopic.CREDIT_ISSUED);
    }

    @KafkaListener(topics = "finish-registration", groupId = "CreditConveyor")
    public void consumeFinishRegistration(String message) {
        log.info("Received finish-registration message: {}", message);
        EmailMessage emailMessage = objectMapper.convertValue(message, EmailMessage.class);
        mailSenderService.sendEmailByTheme(emailMessage, KafkaTopic.FINISH_REGISTRATION);
    }

    @KafkaListener(topics = "send-documents", groupId = "CreditConveyor")
    public void consumeSendDocuments(String message) {
        log.info("Received send-documents message: {}", message);
        EmailMessage emailMessage = objectMapper.convertValue(message, EmailMessage.class);
        mailSenderService.sendEmailByTheme(emailMessage, KafkaTopic.SEND_DOCUMENTS);
    }

    @KafkaListener(topics = "send-ses", groupId = "CreditConveyor")
    public void consumeSendSes(String message) {
        log.info("Received send-ses message: {}", message);
        EmailMessage emailMessage = objectMapper.convertValue(message, EmailMessage.class);
        mailSenderService.sendEmailByTheme(emailMessage, KafkaTopic.SEND_SES);
    }
}

