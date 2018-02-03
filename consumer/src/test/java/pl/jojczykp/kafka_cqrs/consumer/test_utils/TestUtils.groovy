package pl.jojczykp.kafka_cqrs.consumer.test_utils

import pl.jojczykp.kafka_cqrs.consumer.ConsumerDocument
import pl.jojczykp.kafka_cqrs.consumer.ConsumerResponse

import static java.util.UUID.randomUUID
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric

final class TestUtils {

    private TestUtils() {}

    static ConsumerDocument randomConsumerDocument() {
        return ConsumerDocument.builder()
                .id(randomUUID())
                .author(randomAlphabetic(10))
                .text(randomAlphanumeric(10, 50))
                .build()
    }

    static ConsumerResponse randomConsumerResponse() {
        return ConsumerResponse.builder()
                .id(randomUUID())
                .author(randomAlphabetic(10))
                .text(randomAlphanumeric(10, 50))
                .build()
    }
}
