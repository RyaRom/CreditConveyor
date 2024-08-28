package controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import service.MailSenderService;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Contoroller")
public class MailController {

    private final MailSenderService mailSenderService;

    @GetMapping("/send-email")
    public String sendEmail() throws MessagingException {
        log.info("\n\nCONTOLLER ACESSED\n\n");
        mailSenderService.sendMail("credit.conveyor.app@yandex.ru", "spring test", "spring test");
        return "Email sent successfully!";
    }
}
