package com.dossier.service;

import com.dossier.client.DealClient;
import com.dossier.kafka.EmailMessage;
import com.dossier.kafka.KafkaTopic;
import com.dossier.model.dto.ApplicationDTO;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderService {

    private final JavaMailSender sender;

    private final DealClient dealClient;

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

    @SneakyThrows
    public void sendMail(String to, String subject, String text) {
        MimeMessage mimeMessage = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

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
            case SEND_DOCUMENTS -> text = sendDocsMailTemplate.formatted(applicationId);
            case FINISH_REGISTRATION -> text = finishRegistrationMailTemplate.formatted(applicationId);
            case APPLICATION_DENIED -> text = applicationDeniedMailTemplate.formatted(applicationId);
            case SEND_SES -> {
                ApplicationDTO applicationDTO = dealClient.getApplicationById(applicationId);
                text = sendSesMailTemplate.formatted(applicationDTO.getSecCode(), applicationId);
            }
            case CREDIT_ISSUED -> text = creditIssuedMailTemplate.formatted(applicationId);
            case CREATE_DOCUMENTS -> text = createDocumentsMailTemplate.formatted(applicationId);
            default -> throw new RuntimeException("Error in mail service");
        }

        sendMail(address, theme.toString(), text);
    }
}
