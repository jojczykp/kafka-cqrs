package pl.jojczykp.kafka_cqrs.producer.model;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class Document {

    UUID id;

    String author;

    String text;
}
