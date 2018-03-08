package pl.jojczykp.kafka_cqrs.persister.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jojczykp.kafka_cqrs.persister.message.Message;
import pl.jojczykp.kafka_cqrs.persister.model.Document;
import pl.jojczykp.kafka_cqrs.persister.repository.DocumentRepository;

@Service
@Slf4j
public class PersistenceService {

    private DocumentRepository repository;

    @Autowired
    public PersistenceService(DocumentRepository repository) {
        this.repository = repository;
    }

    public void onMessage(Message message) throws JsonProcessingException {
        log.debug("Persisting message");

        Document document = message.getPayload();

        repository.upsertWithDefaultUnset(document);

        log.debug("Persisting message - done");
    }
}
