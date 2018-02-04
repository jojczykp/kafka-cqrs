package pl.jojczykp.kafka_cqrs.consumer.tools;

import pl.jojczykp.kafka_cqrs.consumer.model.Document;
import pl.jojczykp.kafka_cqrs.consumer.rest.ResponseGet;

public class ResponseAssembler {

    public ResponseGet toResponse(Document document) {
        return ResponseGet.builder()
                .id(document.getId())
                .author(document.getAuthor())
                .text(document.getText())
                .build();
    }
}
