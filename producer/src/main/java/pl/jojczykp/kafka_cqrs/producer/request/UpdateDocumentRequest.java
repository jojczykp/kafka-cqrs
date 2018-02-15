package pl.jojczykp.kafka_cqrs.producer.request;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode
@Builder
public class UpdateDocumentRequest {

    public static final String MIME_UPDATE_DOCUMENT = "application/vnd.kafka-cqrs.update-document.1+json";

    private UUID id;
    private String author;
    private String text;
}
