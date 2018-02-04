package pl.jojczykp.kafka_cqrs.notifier.assembler;

import pl.jojczykp.kafka_cqrs.notifier.model.Document;
import pl.jojczykp.kafka_cqrs.notifier.rest.ResponseGet;

public class ResponseAssembler {

    public ResponseGet toResponse(Document document) {
        return ResponseGet.builder()
                .id(document.getId())
                .author(document.getAuthor())
                .text(document.getText())
                .build();
    }
}
