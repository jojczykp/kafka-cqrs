package pl.jojczykp.kafka_cqrs.producer.tools;

import org.springframework.stereotype.Service;
import pl.jojczykp.kafka_cqrs.producer.messaging.Message;
import pl.jojczykp.kafka_cqrs.producer.rest.RequestCreate;
import pl.jojczykp.kafka_cqrs.producer.rest.RequestUpdate;

import java.util.UUID;

@Service
public class MessageAssembler {

    public Message toMessage(UUID id, RequestCreate request) {
        return Message.builder()
                .header(new Message.Header())
                .body(Message.Body.builder()
                        .id(id)
                        .author(request.getAuthor())
                        .text(request.getText())
                        .build())
                .build();
    }

    public Message toMessage(RequestUpdate request) {
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
