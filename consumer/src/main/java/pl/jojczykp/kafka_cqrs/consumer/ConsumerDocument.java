package pl.jojczykp.kafka_cqrs.consumer;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode
@Builder
public class ConsumerDocument {

    private UUID id;
    private String author;
    private String text;
}
