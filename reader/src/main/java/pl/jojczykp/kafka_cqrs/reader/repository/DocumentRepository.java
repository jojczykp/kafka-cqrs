package pl.jojczykp.kafka_cqrs.reader.repository;

import pl.jojczykp.kafka_cqrs.reader.entity.Document;

import java.util.Optional;
import java.util.UUID;

public class DocumentRepository {

    public Optional<Document> find(UUID documentId) {
        return null;
    }
}
