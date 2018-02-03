package pl.jojczykp.kafka_cqrs.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class ProducerController {

    @Autowired
    private ProducerMessageAssembler assembler;

    @Autowired
    private KafkaSender sender;

    @RequestMapping(
            method = POST,
            path = "/documents",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public ProducerDocument create(@RequestBody ProducerDocument document) {
        ProducerMessage message = assembler.toMessage(document);
        sender.send(message);

        return document;
    }
}
