package pl.jojczykp.kafka_cqrs.producer.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Sender {

    private String topic;

    private KafkaTemplate<String, Message> kafkaTemplate;

    public Sender(
            @Value("${kafka.topic}") String topic,
            @Autowired KafkaTemplate<String, Message> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(Message message) {
        kafkaTemplate.send(topic, message);
    }
}
