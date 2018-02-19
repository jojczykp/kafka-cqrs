package pl.jojczykp.kafka_cqrs.notifier.config;

import io.vertx.core.Vertx;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VertxConfig {

    public static final String MESSAGES_ADDRESS = "messages";

    @Bean
    public Vertx vertx() {
        return Vertx.vertx();
    }

}
