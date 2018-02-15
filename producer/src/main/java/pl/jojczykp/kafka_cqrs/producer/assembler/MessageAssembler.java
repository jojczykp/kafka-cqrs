package pl.jojczykp.kafka_cqrs.producer.assembler;

import org.springframework.stereotype.Service;
import pl.jojczykp.kafka_cqrs.producer.message.Message;
import pl.jojczykp.kafka_cqrs.producer.request.CreateDocumentRequest;
import pl.jojczykp.kafka_cqrs.producer.request.UpdateDocumentRequest;

import java.util.UUID;

@Service
public class MessageAssembler {

    public Message toMessage(UUID id, CreateDocumentRequest request) {
        return Message.builder()
                .header(new Message.Header())
                .body(Message.Body.builder()
                        .id(id)
                        .author(request.getAuthor())
                        .text(request.getText())
                        .build())
                .build();
    }

    public Message toMessage(UpdateDocumentRequest request) {
        return Message.builder()
                .header(new Message.Header())
                .body(Message.Body.builder()
                        .id(request.getId())
                        .author(request.getAuthor())
                        .text(request.getText())
                        .build())
                .build();
    }
}
