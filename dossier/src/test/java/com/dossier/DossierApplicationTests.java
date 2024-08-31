package com.dossier;

import com.dossier.kafka.EmailConsumer;
import com.dossier.kafka.EmailMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.Set;

import static com.dossier.kafka.KafkaTopic.SEND_DOCUMENTS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class DossierApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    EmailConsumer emailConsumer;

    @Test
    void sendDocuments() throws Exception {
        EmailMessage message = new EmailMessage("credit.conveyor.app@yandex.ru", SEND_DOCUMENTS.toString(), 50L);
        String messageJson = objectMapper.writeValueAsString(message);
        emailConsumer.consumeSendDocuments(messageJson);
    }

}
