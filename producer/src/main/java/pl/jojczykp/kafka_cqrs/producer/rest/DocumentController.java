package pl.jojczykp.kafka_cqrs.producer.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.jojczykp.kafka_cqrs.producer.assembler.MessageAssembler;
import pl.jojczykp.kafka_cqrs.producer.messaging.Sender;
import pl.jojczykp.kafka_cqrs.producer.messaging.Message;
import pl.jojczykp.kafka_cqrs.producer.tools.IdGenerator;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static pl.jojczykp.kafka_cqrs.producer.rest.RequestCreate.MIME_CREATE_DOCUMENT;
import static pl.jojczykp.kafka_cqrs.producer.rest.RequestUpdate.MIME_UPDATE_DOCUMENT;

@RestController
public class DocumentController {

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private MessageAssembler assembler;

    @Autowired
    private Sender sender;

    @RequestMapping(
            method = POST,
            path = "/documents",
            consumes = MIME_CREATE_DOCUMENT)
    @ResponseStatus(CREATED)
    public void create(@RequestBody RequestCreate request) {
        UUID id = idGenerator.getRandomId();
        Message message = assembler.toMessage(id, request);
        sender.send(message);
    }

    @RequestMapping(
            method = PUT,
            path = "/documents",
            consumes = MIME_UPDATE_DOCUMENT)
    @ResponseStatus(CREATED)
    public void update(@RequestBody RequestUpdate request) {
        Message message = assembler.toMessage(request);
        sender.send(message);
    }
}
