package pl.jojczykp.kafka_cqrs.producer;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProducerMessageAssembler {

    public ProducerDocument toModel(UUID id, CreateDocumentRequest request) {
        return ProducerDocument.builder()
                .id(id)
                .author(request.getAuthor())
                .text(request.getText())
                .build();
    }

    public CreateDocumentMessage toMessage(ProducerDocument document) {
        return CreateDocumentMessage.builder()
                .header(new CreateDocumentMessage.Header())
                .body(document)
                .build();
    }

    public CreateDocumentResponse toResponse(ProducerDocument document) {
        return CreateDocumentResponse.builder()
                .id(document.getId())
                .author(document.getAuthor())
                .text(document.getText())
                .build();
    }
}
