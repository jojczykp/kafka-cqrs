package pl.jojczykp.kafka_cqrs.consumer.tools;

import pl.jojczykp.kafka_cqrs.consumer.model.Document;
import pl.jojczykp.kafka_cqrs.consumer.rest.GetDocumentResponse;

public class ConsumerResponseAssembler {

    public GetDocumentResponse toResponse(Document document) {
        return GetDocumentResponse.builder()
                .id(document.getId())
                .author(document.getAuthor())
                .text(document.getText())
                .build();
    }
}
