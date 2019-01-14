package pl.jojczykp.kafka_cqrs.producer.assembler;

import org.springframework.stereotype.Service;
import pl.jojczykp.kafka_cqrs.producer.message.CreateMessage;
import pl.jojczykp.kafka_cqrs.producer.message.DeleteMessage;
import pl.jojczykp.kafka_cqrs.producer.message.UpdateMessage;
import pl.jojczykp.kafka_cqrs.producer.model.Document;
import pl.jojczykp.kafka_cqrs.producer.request.CreateRequest;
import pl.jojczykp.kafka_cqrs.producer.request.UpdateRequest;

import java.util.UUID;

@Service
public class MessageAssembler {

    public CreateMessage toMessage(UUID id, CreateRequest request) {
        return new CreateMessage(
                Document.builder()
                        .id(id)
                        .author(request.getAuthor())
                        .text(request.getText())
                        .build());
    }

    public UpdateMessage toMessage(UpdateRequest request) {
        return new UpdateMessage(
                Document.builder()
                        .id(request.getId())
                        .author(request.getAuthor())
                        .text(request.getText())
                        .build());
    }

    public DeleteMessage toMessage(UUID id) {
        return new DeleteMessage(id);
    }
}
