package pl.jojczykp.awskafka.producer;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@EqualsAndHashCode
@Builder
public class ProducerMessage {

    private Header header;
    private ProducerDocument body;

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
