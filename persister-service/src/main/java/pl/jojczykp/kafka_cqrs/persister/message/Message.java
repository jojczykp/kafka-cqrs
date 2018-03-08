package pl.jojczykp.kafka_cqrs.persister.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.jojczykp.kafka_cqrs.persister.model.Document;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    private Map<String, Object> header;

    private Document payload;
}
