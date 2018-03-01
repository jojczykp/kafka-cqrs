package pl.jojczykp.kafka_cqrs.producer.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRequest {

    public static final String MIME_UPDATE_DOCUMENT = "application/vnd.kafka-cqrs.update-document.1+json";

    private UUID id;
    private String author;
    private String text;
}
