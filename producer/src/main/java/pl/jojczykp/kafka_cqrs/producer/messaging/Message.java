package pl.jojczykp.kafka_cqrs.producer.messaging;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Getter
@EqualsAndHashCode
@Builder
public class Message {

    private Message.Header header;
    private Message.Body body;

    @Getter
    @EqualsAndHashCode
    public static class Header {
        private UUID messageId;

        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonFormat(shape= STRING)
        private LocalDateTime creationTimestamp;

        public Header() {
            messageId = UUID.randomUUID();
            creationTimestamp = LocalDateTime.now();
        }
    }

    @Getter
    @EqualsAndHashCode
    @Builder
    public static class Body {
        private UUID id;
        private String author;
        private String text;
    }
}
