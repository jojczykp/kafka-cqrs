package pl.jojczykp.kafka_cqrs.reader.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import pl.jojczykp.kafka_cqrs.reader.model.Document;

import java.util.UUID;

public interface DocumentRepository extends CassandraRepository<Document, UUID> {
}
