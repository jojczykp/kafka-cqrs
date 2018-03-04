package pl.jojczykp.kafka_cqrs.persister.message;

import lombok.Data;

import java.util.UUID;

@Data
class MessageHeader {

    private String type;

    private UUID id;

    private String timestamp;
}
