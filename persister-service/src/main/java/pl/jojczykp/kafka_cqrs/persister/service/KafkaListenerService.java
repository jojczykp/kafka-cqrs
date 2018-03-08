package pl.jojczykp.kafka_cqrs.persister.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import pl.jojczykp.kafka_cqrs.persister.message.Message;

@Service
@Slf4j
public class KafkaListenerService {

    @Autowired
    private PersistenceService persistenceService;

    @KafkaListener(topics = "${kafka.topic}", containerFactory = "kafkaListenerContainerFactory")
    public void onMessage(Message message) throws JsonProcessingException {
        log.debug("Message from Kafka received");

        persistenceService.onMessage(message);
    }
}
