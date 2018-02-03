package pl.jojczykp.kafka_cqrs.consumer;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ConsumerResponse {

    private UUID id;
    private String author;
    private String text;
}
