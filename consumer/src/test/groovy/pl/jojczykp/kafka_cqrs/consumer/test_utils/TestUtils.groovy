package pl.jojczykp.kafka_cqrs.consumer.test_utils

import pl.jojczykp.kafka_cqrs.consumer.model.KafkaDocument
import pl.jojczykp.kafka_cqrs.consumer.rest.ResponseGet

import static java.util.UUID.randomUUID
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric

final class TestUtils {

    private TestUtils() {}

    static KafkaDocument randomConsumerDocument() {
        return KafkaDocument.builder()
                .id(randomUUID())
                .author(randomAlphabetic(10))
                .text(randomAlphanumeric(10, 50))
                .build()
    }

    static ResponseGet randomConsumerResponse() {
        return ResponseGet.builder()
                .id(randomUUID())
                .author(randomAlphabetic(10))
                .text(randomAlphanumeric(10, 50))
                .build()
    }
}
