package pl.jojczykp.kafka_cqrs.producer.test_utils

import pl.jojczykp.kafka_cqrs.producer.message.CreateMessage
import pl.jojczykp.kafka_cqrs.producer.message.DeleteMessage
import pl.jojczykp.kafka_cqrs.producer.message.UpdateMessage
import pl.jojczykp.kafka_cqrs.producer.model.Document
import pl.jojczykp.kafka_cqrs.producer.request.CreateRequest
import pl.jojczykp.kafka_cqrs.producer.request.UpdateRequest

import static java.util.UUID.randomUUID
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric

final class TestUtils {

    private TestUtils() {}

    static CreateRequest randomCreateRequest() {
        return CreateRequest.builder()
                .author(randomAlphabetic(10))
                .text(randomAlphanumeric(10, 50))
                .build()
    }

    static UpdateRequest randomUpdateRequest() {
        return UpdateRequest.builder()
                .author(randomAlphabetic(10))
                .text(randomAlphanumeric(10, 50))
                .build()
    }

    static CreateMessage randomCreateMessage() {
        return new CreateMessage(randomMessageBody())
    }

    static UpdateMessage randomUpdateMessage() {
        return new UpdateMessage(randomMessageBody())
    }

    static DeleteMessage randomDeleteMessage() {
        return new DeleteMessage(randomUUID())
    }

    static Document randomMessageBody() {
        return Document.builder()
                .id(randomUUID())
                .author(randomAlphabetic(10))
                .text(randomAlphanumeric(10, 50))
                .build()
    }
}
