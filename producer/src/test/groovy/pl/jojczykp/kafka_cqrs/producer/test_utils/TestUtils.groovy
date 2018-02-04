package pl.jojczykp.kafka_cqrs.producer.test_utils

import pl.jojczykp.kafka_cqrs.producer.messaging.CreateDocumentMessage
import pl.jojczykp.kafka_cqrs.producer.model.Document
import pl.jojczykp.kafka_cqrs.producer.rest.CreateDocumentRequest

import static java.util.UUID.randomUUID
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric

final class TestUtils {

    private TestUtils() {}

    static CreateDocumentRequest randomCreateDocumentRequest() {
        return CreateDocumentRequest.builder()
                .author(randomAlphabetic(10))
                .text(randomAlphanumeric(10, 50))
                .build()
    }

    static CreateDocumentMessage randomCreateDocumentMessage() {
        return CreateDocumentMessage.builder()
                .header(randomCreateDocumentMessageHeader())
                .body(randomProducerDocument())
                .build()
    }

    static CreateDocumentMessage.Header randomCreateDocumentMessageHeader() {
        return new CreateDocumentMessage.Header()
    }

    static Document randomProducerDocument() {
        return Document.builder()
                .id(randomUUID())
                .author(randomAlphabetic(10))
                .text(randomAlphanumeric(10, 50))
                .build()
    }
}
