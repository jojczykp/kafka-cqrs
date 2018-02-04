package pl.jojczykp.kafka_cqrs.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static pl.jojczykp.kafka_cqrs.producer.CreateDocumentRequest.MIME_CREATE_DOCUMENT;
import static pl.jojczykp.kafka_cqrs.producer.CreateDocumentResponse.MIME_ACTUAL_DOCUMENT;

@RestController
public class ProducerController {

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private ProducerMessageAssembler assembler;

    @Autowired
    private KafkaSender sender;

    @RequestMapping(
            method = POST,
            path = "/documents",
            consumes = MIME_CREATE_DOCUMENT,
            produces = MIME_ACTUAL_DOCUMENT)
    @ResponseStatus(CREATED)
    public CreateDocumentResponse create(@RequestBody CreateDocumentRequest request) {
        UUID id = idGenerator.getRandomId();
        ProducerDocument document = assembler.toModel(id, request);

        CreateDocumentMessage message = assembler.toMessage(document);
        sender.send(message);

        CreateDocumentResponse response = assembler.toResponse(document);
        return response;
    }
}
