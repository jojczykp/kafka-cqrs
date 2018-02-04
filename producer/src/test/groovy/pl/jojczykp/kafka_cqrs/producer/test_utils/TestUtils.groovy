package pl.jojczykp.kafka_cqrs.producer.test_utils

import pl.jojczykp.kafka_cqrs.producer.messaging.Message
import pl.jojczykp.kafka_cqrs.producer.rest.RequestCreate
import pl.jojczykp.kafka_cqrs.producer.rest.RequestUpdate

import static java.util.UUID.randomUUID
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric

final class TestUtils {

    private TestUtils() {}

    static RequestCreate randomCreateDocumentRequest() {
        return RequestCreate.builder()
                .author(randomAlphabetic(10))
                .text(randomAlphanumeric(10, 50))
                .build()
    }

    static RequestUpdate randomUpdateDocumentRequest() {
        return RequestUpdate.builder()
                .author(randomAlphabetic(10))
                .text(randomAlphanumeric(10, 50))
                .build()
    }

    static Message randomDocumentMessage() {
        return Message.builder()
                .header(randomDocumentMessageHeader())
                .body(randomDocumentMessageBody())
                .build()
    }

    static Message.Header randomDocumentMessageHeader() {
        return new Message.Header()
    }


    static Message.Body randomDocumentMessageBody() {
        return Message.Body.builder()
                .id(randomUUID())
                .author(randomAlphabetic(10))
                .text(randomAlphanumeric(10, 50))
                .build()
    }
}
