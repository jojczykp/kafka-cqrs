package pl.jojczykp.kafka_cqrs.consumer.rest;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class GetDocumentResponse {

    public static final String MIME_CREATE_DOCUMENT = "application/vnd.kafka-cqrs.document.1+json";

    private UUID id;
    private String author;
    private String text;
}
