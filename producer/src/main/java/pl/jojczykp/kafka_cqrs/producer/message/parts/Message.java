package pl.jojczykp.kafka_cqrs.producer.message.parts;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public abstract class Message {

    protected MessageHeader header;

    protected MessageBody body;

    protected Message(MessageType type, MessageBody body) {
        this.header = new MessageHeader(type);
        this.body = body;
    }
}
