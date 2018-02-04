package pl.jojczykp.kafka_cqrs.producer.tools;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IdGenerator {

    public UUID getRandomId() {
        return UUID.randomUUID();
    }
}
