package pl.jojczykp.kafka_cqrs.producer.message;

import pl.jojczykp.kafka_cqrs.producer.message.parts.Message;
import pl.jojczykp.kafka_cqrs.producer.model.Document;
import pl.jojczykp.kafka_cqrs.producer.message.parts.MessageType;

public class CreateMessage extends Message {

    public CreateMessage(Document body) {
        super(MessageType.CREATE, body);
    }
}
