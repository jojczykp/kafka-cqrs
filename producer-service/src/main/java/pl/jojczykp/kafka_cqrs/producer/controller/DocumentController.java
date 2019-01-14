package pl.jojczykp.kafka_cqrs.producer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.jojczykp.kafka_cqrs.producer.assembler.MessageAssembler;
import pl.jojczykp.kafka_cqrs.producer.assembler.ResponseAssembler;
import pl.jojczykp.kafka_cqrs.producer.message.CreateMessage;
import pl.jojczykp.kafka_cqrs.producer.message.DeleteMessage;
import pl.jojczykp.kafka_cqrs.producer.message.UpdateMessage;
import pl.jojczykp.kafka_cqrs.producer.request.CreateRequest;
import pl.jojczykp.kafka_cqrs.producer.request.UpdateRequest;
import pl.jojczykp.kafka_cqrs.producer.response.CreateResponse;
import pl.jojczykp.kafka_cqrs.producer.service.IdService;
import pl.jojczykp.kafka_cqrs.producer.service.SenderService;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static pl.jojczykp.kafka_cqrs.producer.request.CreateRequest.MIME_CREATE_DOCUMENT;
import static pl.jojczykp.kafka_cqrs.producer.request.UpdateRequest.MIME_UPDATE_DOCUMENT;
import static pl.jojczykp.kafka_cqrs.producer.response.CreateResponse.MIME_DOCUMENT_ID;

@RestController
public class DocumentController {

    @Autowired
    private IdService idService;

    @Autowired
    private MessageAssembler messageAssembler;

    @Autowired
    private ResponseAssembler responseAssembler;

    @Autowired
    private SenderService senderService;

    @RequestMapping(
            method = POST,
            path = "/documents",
            consumes = MIME_CREATE_DOCUMENT,
            produces = MIME_DOCUMENT_ID)
    @ResponseStatus(CREATED)
    public CreateResponse create(@RequestBody CreateRequest request) {
        UUID id = idService.getRandomId();
        CreateMessage message = messageAssembler.toMessage(id, request);
        senderService.send(message);

        return responseAssembler.toResponse(id);
    }

    @RequestMapping(
            method = PUT,
            path = "/documents",
            consumes = MIME_UPDATE_DOCUMENT)
    @ResponseStatus(OK)
    public void update(@RequestBody UpdateRequest request) {
        UpdateMessage message = messageAssembler.toMessage(request);
        senderService.send(message);
    }

    @RequestMapping(
            method = DELETE,
            path = "/documents/{document_id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable("document_id") UUID documentId) {
        DeleteMessage message = messageAssembler.toMessage(documentId);
        senderService.send(message);
    }
}
