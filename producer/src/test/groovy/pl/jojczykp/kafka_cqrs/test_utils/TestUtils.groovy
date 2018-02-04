package pl.jojczykp.kafka_cqrs.test_utils

import pl.jojczykp.kafka_cqrs.producer.CreateDocumentRequest
import pl.jojczykp.kafka_cqrs.producer.CreateDocumentResponse
import pl.jojczykp.kafka_cqrs.producer.ProducerDocument
import pl.jojczykp.kafka_cqrs.producer.CreateDocumentMessage

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

    static ProducerDocument randomProducerDocument() {
        return ProducerDocument.builder()
                .id(randomUUID())
                .author(randomAlphabetic(10))
                .text(randomAlphanumeric(10, 50))
                .build()
    }

    static CreateDocumentResponse randomCreateDocumentResponse() {
        return CreateDocumentResponse.builder()
                .id(randomUUID())
                .author(randomAlphabetic(10))
                .text(randomAlphanumeric(10, 50))
                .build()
    }
}
