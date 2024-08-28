package com.dossier.kafka;

import com.dossier.model.EmailMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EmailConsumer {

    @KafkaListener(topics = "application-denied", groupId = "CreditConveyor")
    public void consumeApplicationDenied(EmailMessage message) {
        System.out.println("Received application-denied message: " + message);
    }

    @KafkaListener(topics = "create-documents", groupId = "CreditConveyor")
    public void consumeCreateDocuments(EmailMessage message) {
        System.out.println("Received create-documents message: " + message);
    }

    @KafkaListener(topics = "credit-issued", groupId = "CreditConveyor")
    public void consumeCreditIssued(EmailMessage message) {
        System.out.println("Received credit-issued message: " + message);
    }

    @KafkaListener(topics = "finish-registration", groupId = "CreditConveyor")
    public void consumeFinishRegistration(EmailMessage message) {
        System.out.println("Received finish-registration message: " + message);
    }

    @KafkaListener(topics = "send-documents", groupId = "CreditConveyor")
    public void consumeSendDocuments(EmailMessage message) {
        System.out.println("Received send-documents message: " + message);
    }

    @KafkaListener(topics = "send-ses", groupId = "CreditConveyor")
    public void consumeSendSes(EmailMessage message) {
        System.out.println("Received send-ses message: " + message);
    }
}

