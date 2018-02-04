package pl.jojczykp.kafka_cqrs.notifier.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.jojczykp.kafka_cqrs.notifier.model.Message;
import pl.jojczykp.kafka_cqrs.notifier.assembler.NotificationAssembler;
import pl.jojczykp.kafka_cqrs.notifier.messaging.Listener;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static pl.jojczykp.kafka_cqrs.notifier.rest.Notification.MIME_NOTIFICATIONS;

@RestController
public class NotificationController {

    @Autowired
    private Listener listener;

    @Autowired
    private NotificationAssembler assembler;

    @RequestMapping(
            method = GET,
            path = "/documents/notifications",
            produces = MIME_NOTIFICATIONS)
    public void listen() {
    }
}
