package pl.jojczykp.kafka_cqrs.consumer.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.jojczykp.kafka_cqrs.consumer.model.KafkaDocument;
import pl.jojczykp.kafka_cqrs.consumer.assembler.ResponseAssembler;
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
    private ResponseAssembler assembler;

    @RequestMapping(
            method = GET,
            path = "/documents/{document_id}",
            produces = MIME_DOCUMENT)
    public ResponseEntity<ResponseGet> get(@PathVariable("document_id") UUID documentId) {
        Optional<KafkaDocument> maybeDocument = reader.find(documentId);

        if (maybeDocument.isPresent()) {
            KafkaDocument kafkaDocument = maybeDocument.get();
            ResponseGet response = assembler.toResponse(kafkaDocument);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
