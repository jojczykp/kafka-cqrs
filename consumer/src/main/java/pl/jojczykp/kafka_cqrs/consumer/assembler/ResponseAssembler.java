package pl.jojczykp.kafka_cqrs.consumer.assembler;

import pl.jojczykp.kafka_cqrs.consumer.model.KafkaDocument;
import pl.jojczykp.kafka_cqrs.consumer.rest.ResponseGet;

public class ResponseAssembler {

    public ResponseGet toResponse(KafkaDocument kafkaDocument) {
        return ResponseGet.builder()
                .id(kafkaDocument.getId())
                .author(kafkaDocument.getAuthor())
                .text(kafkaDocument.getText())
                .build();
    }
}
