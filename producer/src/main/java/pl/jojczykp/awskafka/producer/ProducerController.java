package pl.jojczykp.awskafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
public class ProducerController {

    @Autowired
    private ProducerMessageAssembler assembler;

    @Autowired
    private KafkaSender sender;

    @RequestMapping("/document")
    @ResponseStatus(CREATED)
    public void create(@RequestBody ProducerDocument document) {
        ProducerMessage message = assembler.toMessage(document);
        sender.send(message);
    }
}
