package pl.jojczykp.kafka_cqrs.producer.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CreateResponse {

    public static final String MIME_DOCUMENT_ID = "application/vnd.kafka-cqrs.document-id.1+json";

    private String id;
}
