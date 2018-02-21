package pl.jojczykp.kafka_cqrs.reader.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.jojczykp.kafka_cqrs.reader.entity.Document;
import pl.jojczykp.kafka_cqrs.reader.repository.DocumentRepository;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class DocumentController {

    @Autowired
    private DocumentRepository documentRepository;

    @RequestMapping(
            method = GET,
            path = "/documents/{document_id}",
            produces = "application/vnd.kafka-cqrs.document.1+json")
    public ResponseEntity<Document> get(@PathVariable("document_id") UUID documentId) {
        Optional<Document> maybeDocument = documentRepository.find(documentId);

        if (maybeDocument.isPresent()) {
            Document document = maybeDocument.get();
            return ResponseEntity.ok(document);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
