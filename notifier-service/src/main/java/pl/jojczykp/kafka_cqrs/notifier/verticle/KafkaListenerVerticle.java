package pl.jojczykp.kafka_cqrs.notifier.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static pl.jojczykp.kafka_cqrs.notifier.config.VertxConfig.MESSAGES_ADDRESS;

@Component
@Slf4j
public class KafkaListenerVerticle extends AbstractVerticle {

    @Autowired
    private Vertx vertx;

    @KafkaListener(topics = "${kafka.topic}", containerFactory = "kafkaListenerContainerFactory")
    public void onMessage(byte[] message) {
        log.debug("Received message from Kafka");

        vertx.eventBus().publish(MESSAGES_ADDRESS, message);

        log.debug("Propagated message to event bus");
    }
}
