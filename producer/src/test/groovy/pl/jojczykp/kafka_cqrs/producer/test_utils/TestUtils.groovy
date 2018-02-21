package pl.jojczykp.kafka_cqrs.producer.test_utils

import pl.jojczykp.kafka_cqrs.producer.message.CreateMessage
import pl.jojczykp.kafka_cqrs.producer.message.UpdateMessage
import pl.jojczykp.kafka_cqrs.producer.message.parts.MessageBody
import pl.jojczykp.kafka_cqrs.producer.request.CreateDocumentRequest
import pl.jojczykp.kafka_cqrs.producer.request.UpdateDocumentRequest

import static java.util.UUID.randomUUID
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric

final class TestUtils {

    private TestUtils() {}

    static CreateDocumentRequest randomCreateRequest() {
        return CreateDocumentRequest.builder()
                .author(randomAlphabetic(10))
                .text(randomAlphanumeric(10, 50))
                .build()
    }

    static UpdateDocumentRequest randomUpdateRequest() {
        return UpdateDocumentRequest.builder()
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

    static MessageBody randomMessageBody() {
        return MessageBody.builder()
                .id(randomUUID())
                .author(randomAlphabetic(10))
                .text(randomAlphanumeric(10, 50))
                .build()
    }
}
