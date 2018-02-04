package pl.jojczykp.kafka_cqrs.producer;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@Builder
public class CreateDocumentRequest {

    public static final String MIME_CREATE_DOCUMENT = "application/vnd.kafka-cqrs.create-document.1+json";

    private String author;
    private String text;
}
