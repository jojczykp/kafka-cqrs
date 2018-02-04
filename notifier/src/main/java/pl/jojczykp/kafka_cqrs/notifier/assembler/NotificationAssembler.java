package pl.jojczykp.kafka_cqrs.notifier.assembler;

import pl.jojczykp.kafka_cqrs.notifier.model.Message;
import pl.jojczykp.kafka_cqrs.notifier.rest.Notification;

public class NotificationAssembler {

    public Notification toResponse(Message message) {
        return Notification.builder()
                .id(message.getId())
                .author(message.getAuthor())
                .text(message.getText())
                .build();
    }
}
