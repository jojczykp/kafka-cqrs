package pl.jojczykp.kafka_cqrs.producer.message.parts;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class MessageBody {

    UUID id;

    String author;

    String text;
}
