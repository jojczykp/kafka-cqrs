package pl.jojczykp.kafka_cqrs.consumer.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.jojczykp.kafka_cqrs.consumer.model.Document;
import pl.jojczykp.kafka_cqrs.consumer.tools.ConsumerResponseAssembler;
import pl.jojczykp.kafka_cqrs.consumer.messaging.KafkaReader;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static pl.jojczykp.kafka_cqrs.consumer.rest.ResponseGet.MIME_DOCUMENT;

@RestController
public class DocumentController {

    @Autowired
    private KafkaReader reader;

    @Autowired
    private ConsumerResponseAssembler assembler;

    @RequestMapping(
            method = GET,
            path = "/documents/{document_id}",
            produces = MIME_DOCUMENT)
    public ResponseEntity<ResponseGet> get(@PathVariable("document_id") UUID documentId) {
        Optional<Document> maybeDocument = reader.find(documentId);

        if (maybeDocument.isPresent()) {
            Document document = maybeDocument.get();
            ResponseGet response = assembler.toResponse(document);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
