package pl.jojczykp.kafka_cqrs.producer;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode
@Builder
public class ProducerDocument {

    private UUID id;
    private String author;
    private String text;
}
