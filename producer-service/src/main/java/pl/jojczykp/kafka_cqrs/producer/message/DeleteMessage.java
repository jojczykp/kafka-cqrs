package pl.jojczykp.kafka_cqrs.producer.message;

import pl.jojczykp.kafka_cqrs.producer.message.parts.Message;
import pl.jojczykp.kafka_cqrs.producer.message.parts.MessageType;
import pl.jojczykp.kafka_cqrs.producer.model.Document;

import java.util.UUID;

public class DeleteMessage extends Message {

    public DeleteMessage(UUID id) {
        super(MessageType.DELETE, Document.builder().id(id).build());
    }
}
