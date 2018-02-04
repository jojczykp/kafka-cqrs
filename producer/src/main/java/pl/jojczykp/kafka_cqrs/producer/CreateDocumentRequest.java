package pl.jojczykp.kafka_cqrs.producer;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@Builder
public class CreateDocumentRequest {

    private String author;
    private String text;
}
