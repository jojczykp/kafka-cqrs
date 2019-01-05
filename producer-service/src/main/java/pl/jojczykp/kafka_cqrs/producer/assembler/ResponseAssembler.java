package pl.jojczykp.kafka_cqrs.producer.assembler;

import org.springframework.stereotype.Service;
import pl.jojczykp.kafka_cqrs.producer.response.CreateResponse;

import java.util.UUID;

@Service
public class ResponseAssembler {

    public CreateResponse toResponse(UUID id) {
        return new CreateResponse(id.toString());
    }
}
