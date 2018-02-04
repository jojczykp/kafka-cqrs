package pl.jojczykp.kafka_cqrs.consumer.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode
@Builder
public class Document {

    private UUID id;
    private String author;
    private String text;
}
