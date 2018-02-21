package pl.jojczykp.kafka_cqrs.producer.message;

import pl.jojczykp.kafka_cqrs.producer.message.parts.Message;
import pl.jojczykp.kafka_cqrs.producer.message.parts.MessageBody;
import pl.jojczykp.kafka_cqrs.producer.message.parts.MessageType;

public class UpdateMessage extends Message {

    public UpdateMessage(MessageBody body) {
        super(MessageType.UPDATE, body);
    }
}
