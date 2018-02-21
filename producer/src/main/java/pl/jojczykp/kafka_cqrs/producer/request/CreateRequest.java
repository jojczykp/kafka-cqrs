package pl.jojczykp.kafka_cqrs.producer.request;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@Builder
public class CreateRequest {

    public static final String MIME_CREATE_DOCUMENT = "application/vnd.kafka-cqrs.create-document.1+json";

    private String author;
    private String text;
}
