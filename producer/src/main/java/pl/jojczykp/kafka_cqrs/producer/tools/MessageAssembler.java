package pl.jojczykp.kafka_cqrs.producer.tools;

import org.springframework.stereotype.Service;
import pl.jojczykp.kafka_cqrs.producer.messaging.CreateDocumentMessage;
import pl.jojczykp.kafka_cqrs.producer.model.Document;
import pl.jojczykp.kafka_cqrs.producer.rest.CreateDocumentRequest;
import pl.jojczykp.kafka_cqrs.producer.rest.CreateDocumentResponse;

import java.util.UUID;

@Service
public class MessageAssembler {

    public Document toModel(UUID id, CreateDocumentRequest request) {
        return Document.builder()
                .id(id)
                .author(request.getAuthor())
                .text(request.getText())
                .build();
    }

    public CreateDocumentMessage toMessage(Document document) {
        return CreateDocumentMessage.builder()
                .header(new CreateDocumentMessage.Header())
                .body(document)
                .build();
    }

    public CreateDocumentResponse toResponse(Document document) {
        return CreateDocumentResponse.builder()
                .id(document.getId())
                .author(document.getAuthor())
                .text(document.getText())
                .build();
    }
}
