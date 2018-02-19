package pl.jojczykp.kafka_cqrs.notifier.verticle;

import io.vertx.core.AbstractVerticle;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static pl.jojczykp.kafka_cqrs.notifier.config.VertxConfig.MESSAGES_ADDRESS;

@Component
public class KafkaListenerVerticle extends AbstractVerticle {

    @KafkaListener(topics = "${kafka.topic}", containerFactory = "kafkaListenerContainerFactory")
    public void onMessage(byte[] message) {
        vertx.eventBus().publish(MESSAGES_ADDRESS, message);
    }

}
