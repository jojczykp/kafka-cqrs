package pl.jojczykp.kafka_cqrs.producer.message.parts;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import pl.jojczykp.kafka_cqrs.producer.model.Document;

@Getter
@EqualsAndHashCode
public abstract class Message {

    protected MessageHeader header;

    protected Document payload;

    protected Message(MessageType type, Document payload) {
        this.header = new MessageHeader(type);
        this.payload = payload;
    }
}
