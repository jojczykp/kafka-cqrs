package pl.jojczykp.kafka_cqrs.notifier.rest;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class Notification {

    public static final String MIME_NOTIFICATIONS = "application/vnd.kafka-cqrs.document.notification.1+json";

    private UUID id;
    private String author;
    private String text;
}
