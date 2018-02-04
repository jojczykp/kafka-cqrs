package pl.jojczykp.kafka_cqrs.reader.assembler;

import pl.jojczykp.kafka_cqrs.reader.model.Document;
import pl.jojczykp.kafka_cqrs.reader.rest.ResponseGet;

public class ResponseAssembler {

    public ResponseGet toResponse(Document document) {
        return ResponseGet.builder()
                .id(document.getId())
                .author(document.getAuthor())
                .text(document.getText())
                .build();
    }
}
