package pl.jojczykp.kafka_cqrs.reader.model;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Value
@Builder
@Table("documents")
public class Document {

    @PrimaryKey
    UUID id;
    String author;
    String text;
}
