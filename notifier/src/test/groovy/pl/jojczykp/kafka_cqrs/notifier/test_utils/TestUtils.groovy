package pl.jojczykp.kafka_cqrs.notifier.test_utils

import pl.jojczykp.kafka_cqrs.notifier.model.Message
import pl.jojczykp.kafka_cqrs.notifier.rest.Notification

import static java.util.UUID.randomUUID
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric

final class TestUtils {

    private TestUtils() {}

    static Message randomDocument() {
        return Message.builder()
                .id(randomUUID())
                .author(randomAlphabetic(10))
                .text(randomAlphanumeric(10, 50))
                .build()
    }

    static Notification randomResponse() {
        return Notification.builder()
                .id(randomUUID())
                .author(randomAlphabetic(10))
                .text(randomAlphanumeric(10, 50))
                .build()
    }
}
