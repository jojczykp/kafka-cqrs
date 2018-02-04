package pl.jojczykp.kafka_cqrs.producer.rest;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode
@Builder
public class CreateDocumentResponse {

    public static final String MIME_ACTUAL_DOCUMENT = "application/vnd.kafka-cqrs.actual-document.1+json";

    private UUID id;
    private String author;
    private String text;
}
