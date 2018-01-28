package pl.jojczykp.kafka_cqrs.test_utils;

import pl.jojczykp.kafka_cqrs.producer.ProducerDocument;
import pl.jojczykp.kafka_cqrs.producer.ProducerMessage;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

public final class TestUtils {

    private TestUtils() {}

    public static ProducerMessage randomProducerMessage() {
        return ProducerMessage.builder()
                .header(randomProducerMessageHeader())
                .body(randomProducerDocument())
                .build();
    }

    public static ProducerMessage.Header randomProducerMessageHeader() {
        return new ProducerMessage.Header();
    }

    public static ProducerDocument randomProducerDocument() {
        return ProducerDocument.builder()
                .id(randomUUID())
                .author(randomAlphabetic(10))
                .text(randomAlphanumeric(10, 50))
                .build();
    }
}
