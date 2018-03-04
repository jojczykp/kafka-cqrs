package pl.jojczykp.kafka_cqrs.persister.message;

import lombok.Builder;
import lombok.Data;
import pl.jojczykp.kafka_cqrs.persister.model.Document;

import java.util.Map;

@Data
@Builder
public class Message {

    private Map<String, Object> header;

    private Document payload;
}
