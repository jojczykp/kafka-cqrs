package pl.jojczykp.kafka_cqrs.producer.message.parts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Value
class MessageHeader {

    MessageType type;

    UUID id;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = STRING)
    LocalDateTime creationTimestamp;

    MessageHeader(MessageType type) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.creationTimestamp = LocalDateTime.now();
    }
}
