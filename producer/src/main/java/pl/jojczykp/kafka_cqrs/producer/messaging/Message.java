package pl.jojczykp.kafka_cqrs.producer.messaging;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

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
