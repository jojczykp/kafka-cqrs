package pl.jojczykp.kafka_cqrs.consumer;

public class ConsumerResponseAssembler {

    public ConsumerResponse toResponse(ConsumerDocument document) {
        return ConsumerResponse.builder()
                .id(document.getId())
                .author(document.getAuthor())
                .text(document.getText())
                .build();
    }
}
