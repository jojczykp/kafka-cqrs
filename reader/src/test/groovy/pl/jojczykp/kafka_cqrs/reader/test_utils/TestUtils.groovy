package pl.jojczykp.kafka_cqrs.reader.test_utils

import pl.jojczykp.kafka_cqrs.reader.model.Document
import pl.jojczykp.kafka_cqrs.reader.rest.ResponseGet

import static java.util.UUID.randomUUID
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric

final class TestUtils {

    private TestUtils() {}

    static Document randomDocument() {
        return Document.builder()
                .id(randomUUID())
                .author(randomAlphabetic(10))
                .text(randomAlphanumeric(10, 50))
                .build()
    }

    static ResponseGet randomResponse() {
        return ResponseGet.builder()
                .id(randomUUID())
                .author(randomAlphabetic(10))
                .text(randomAlphanumeric(10, 50))
                .build()
    }
}
