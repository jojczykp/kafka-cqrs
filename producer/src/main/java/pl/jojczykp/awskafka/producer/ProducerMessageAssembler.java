package pl.jojczykp.awskafka.producer;

import org.springframework.stereotype.Service;

@Service
public class ProducerMessageAssembler {

    public ProducerMessage toMessage(ProducerDocument document) {
        return ProducerMessage.builder()
                .header(new ProducerMessage.Header())
                .body(document)
                .build();
    }
}
