package pl.jojczykp.kafka_cqrs.consumer.messaging;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import pl.jojczykp.kafka_cqrs.consumer.model.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@EqualsAndHashCode
@Builder
public class ConsumerMessage {

    private Header header;
    private Document body;

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

}
