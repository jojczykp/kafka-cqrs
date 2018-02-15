package pl.jojczykp.kafka_cqrs.producer.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IdService {

    public UUID getRandomId() {
        return UUID.randomUUID();
    }
}
