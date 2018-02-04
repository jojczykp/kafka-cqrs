package pl.jojczykp.kafka_cqrs.consumer;

public class ConsumerResponseAssembler {

    public GetDocumentResponse toResponse(ConsumerDocument document) {
        return GetDocumentResponse.builder()
                .id(document.getId())
                .author(document.getAuthor())
                .text(document.getText())
                .build();
    }
}
