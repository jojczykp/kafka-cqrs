package pl.jojczykp.kafka_cqrs.consumer.test_utils

import pl.jojczykp.kafka_cqrs.consumer.ConsumerDocument
import pl.jojczykp.kafka_cqrs.consumer.GetDocumentResponse

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

    static GetDocumentResponse randomConsumerResponse() {
        return GetDocumentResponse.builder()
                .id(randomUUID())
                .author(randomAlphabetic(10))
                .text(randomAlphanumeric(10, 50))
                .build()
    }
}