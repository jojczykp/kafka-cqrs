package pl.jojczykp.kafka_cqrs.producer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.jojczykp.kafka_cqrs.producer.service.IdService;
import pl.jojczykp.kafka_cqrs.producer.message.Message;
import pl.jojczykp.kafka_cqrs.producer.assembler.MessageAssembler;
import pl.jojczykp.kafka_cqrs.producer.request.CreateDocumentRequest;
import pl.jojczykp.kafka_cqrs.producer.request.UpdateDocumentRequest;
import pl.jojczykp.kafka_cqrs.producer.service.SenderService;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static pl.jojczykp.kafka_cqrs.producer.request.CreateDocumentRequest.MIME_CREATE_DOCUMENT;
import static pl.jojczykp.kafka_cqrs.producer.request.UpdateDocumentRequest.MIME_UPDATE_DOCUMENT;

@RestController
public class DocumentController {

    @Autowired
    private IdService idService;

    @Autowired
    private MessageAssembler messageAssembler;

    @Autowired
    private SenderService senderService;

    @RequestMapping(
            method = POST,
            path = "/documents",
            consumes = MIME_CREATE_DOCUMENT)
    @ResponseStatus(CREATED)
    public void create(@RequestBody CreateDocumentRequest request) {
        UUID id = idService.getRandomId();
        Message message = messageAssembler.toMessage(id, request);
        senderService.send(message);
    }

    @RequestMapping(
            method = PUT,
            path = "/documents",
            consumes = MIME_UPDATE_DOCUMENT)
    @ResponseStatus(OK)
    public void update(@RequestBody UpdateDocumentRequest request) {
        Message message = messageAssembler.toMessage(request);
        senderService.send(message);
    }
}
