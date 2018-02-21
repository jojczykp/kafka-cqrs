package pl.jojczykp.kafka_cqrs.producer.assembler;

import org.springframework.stereotype.Service;
import pl.jojczykp.kafka_cqrs.producer.message.CreateMessage;
import pl.jojczykp.kafka_cqrs.producer.message.UpdateMessage;
import pl.jojczykp.kafka_cqrs.producer.message.parts.MessageBody;
import pl.jojczykp.kafka_cqrs.producer.request.CreateDocumentRequest;
import pl.jojczykp.kafka_cqrs.producer.request.UpdateDocumentRequest;

import java.util.UUID;

@Service
public class MessageAssembler {

    public CreateMessage toMessage(UUID id, CreateDocumentRequest request) {
        return new CreateMessage(
                MessageBody.builder()
                        .id(id)
                        .author(request.getAuthor())
                        .text(request.getText())
                        .build());
    }

    public UpdateMessage toMessage(UpdateDocumentRequest request) {
        return new UpdateMessage(
                MessageBody.builder()
                        .id(request.getId())
                        .author(request.getAuthor())
                        .text(request.getText())
                        .build());
    }
}
