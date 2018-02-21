package pl.jojczykp.kafka_cqrs.producer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.jojczykp.kafka_cqrs.producer.message.parts.Message;

@Service
@Slf4j
public class SenderService {

    private String topic;

    private KafkaTemplate<String, Message> kafkaTemplate;

    public SenderService(
            @Value("${kafka.topic}") String topic,
            @Autowired KafkaTemplate<String, Message> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(Message message) {
        if (log.isDebugEnabled()) {
            log.debug("Sending message for document id " + message.getBody().getId());
        }

        kafkaTemplate.send(topic, message);
    }
}
