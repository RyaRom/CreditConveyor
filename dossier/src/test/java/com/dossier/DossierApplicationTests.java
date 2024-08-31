package com.dossier;

import com.dossier.kafka.EmailConsumer;
import com.dossier.kafka.EmailMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static com.dossier.kafka.KafkaTopic.SEND_DOCUMENTS;

@SpringBootTest
@AutoConfigureMockMvc
class DossierApplicationTests {

    @Autowired
    EmailConsumer emailConsumer;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void sendDocuments() throws Exception {
        EmailMessage message = new EmailMessage("credit.conveyor.app@yandex.ru", SEND_DOCUMENTS.toString(), 50L);
        String messageJson = objectMapper.writeValueAsString(message);
        emailConsumer.consumeSendDocuments(messageJson);
    }

}
