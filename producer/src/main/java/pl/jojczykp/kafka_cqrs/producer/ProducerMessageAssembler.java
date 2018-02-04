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

    public ProducerMessage toMessage(ProducerDocument document) {
        return ProducerMessage.builder()
                .header(new ProducerMessage.Header())
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
