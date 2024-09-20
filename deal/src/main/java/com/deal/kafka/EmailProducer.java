package com.deal.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void sendMessage(String address, KafkaTopic topic, Long applicationId) {
        EmailMessage emailMessage = new EmailMessage(address, topic, applicationId);
        String message = objectMapper.writeValueAsString(emailMessage);
        log.info("Send message {}. To topic {}", message, topic.getTopicName());
        kafkaTemplate.send(topic.getTopicName(), message);
    }
}

