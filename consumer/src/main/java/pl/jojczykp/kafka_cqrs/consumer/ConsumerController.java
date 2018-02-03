package pl.jojczykp.kafka_cqrs.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class ConsumerController {

    @Autowired
    private KafkaReader reader;

    @Autowired
    private ConsumerResponseAssembler assembler;

    @RequestMapping(
            method = GET,
            path = "/documents/{document_id}",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ConsumerResponse> get(@PathVariable("document_id") UUID documentId) {
        Optional<ConsumerDocument> maybeDocument = reader.find(documentId);

        if (maybeDocument.isPresent()) {
            ConsumerDocument document = maybeDocument.get();
            ConsumerResponse response = assembler.toResponse(document);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
