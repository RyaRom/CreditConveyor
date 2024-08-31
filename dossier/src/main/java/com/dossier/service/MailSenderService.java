package com.dossier.service;

import com.dossier.client.DealClient;
import com.dossier.kafka.EmailMessage;
import com.dossier.kafka.KafkaTopic;
import com.dossier.model.dto.ApplicationDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class MailSenderService {

    private final JavaMailSender sender;

    private final DealClient dealClient;

    private final DocumentService documentService;

    @Value("${spring.mail.username}")
    private String serviceMail;


    @Value("${spring.mail.templates.finish-registration}")
    private String finishRegistrationMailTemplate;

    @Value("${spring.mail.templates.create-documents}")
    private String createDocumentsMailTemplate;

    @Value("${spring.mail.templates.application-denied}")
    private String applicationDeniedMailTemplate;

    @Value("${spring.mail.templates.credit-issued}")
    private String creditIssuedMailTemplate;

    @Value("${spring.mail.templates.send-documents}")
    private String sendDocsMailTemplate;

    @Value("${spring.mail.templates.send-ses}")
    private String sendSesMailTemplate;

    @Value("${spring.mail.templates.document.credit-contract}")
    private String creditContractTemplate;

    @SneakyThrows
    public void sendMail(String to, String subject, String text, File... attachments) {
        MimeMessage mimeMessage = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        Arrays.stream(attachments).forEach(file -> {
            try {
                helper.addAttachment(file.getName(), file);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
        helper.setFrom(serviceMail);
        helper.setTo(to);
        helper.setText(text);
        helper.setSubject(subject);

        sender.send(mimeMessage);
    }

    public void sendEmailByTheme(EmailMessage message, KafkaTopic theme) {
        Long applicationId = message.applicationId();
        String address = message.address();

        String text;
        switch (theme) {
            case FINISH_REGISTRATION -> text = finishRegistrationMailTemplate.formatted(applicationId);
            case APPLICATION_DENIED -> text = applicationDeniedMailTemplate.formatted(applicationId);
            case CREATE_DOCUMENTS -> text = createDocumentsMailTemplate.formatted(applicationId);
            case CREDIT_ISSUED -> text = creditIssuedMailTemplate.formatted(applicationId);
            case SEND_SES -> {
                ApplicationDTO applicationDTO = dealClient.getApplicationById(applicationId);
                text = sendSesMailTemplate.formatted(applicationDTO.getSecCode(), applicationId);
            }
            case SEND_DOCUMENTS -> {
                ApplicationDTO applicationDTO = dealClient.getApplicationById(applicationId);
                File creditContract = documentService.createDocument(creditContractTemplate, applicationDTO.getCredit(), applicationDTO.getClient());
                text = sendDocsMailTemplate.formatted(applicationId);
                sendMail(address, theme.toString(), text, creditContract);
            }
            default -> throw new NullPointerException("Error in Kafka consumer");
        }

        if (theme != KafkaTopic.SEND_DOCUMENTS) {
            sendMail(address, theme.toString(), text);
        }
    }
}
