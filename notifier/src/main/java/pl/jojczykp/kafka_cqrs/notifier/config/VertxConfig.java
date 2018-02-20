package pl.jojczykp.kafka_cqrs.notifier.config;

import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class VertxConfig {

    public static final String MESSAGES_ADDRESS = "messages";

    @Bean
    public Vertx vertx() {
        log.info("Creating Vert.x bean");
        Vertx vertx = Vertx.vertx();
        log.info("Creating Vert.x bean done");

        return vertx;
    }

}
