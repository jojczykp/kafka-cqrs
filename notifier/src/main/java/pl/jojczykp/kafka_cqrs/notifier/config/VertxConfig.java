package pl.jojczykp.kafka_cqrs.notifier.config;

import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

@Configuration
@Slf4j
public class VertxConfig {

    public static final String MESSAGES_ADDRESS = "messages";

    private Vertx vertx;

    @Bean
    public Vertx vertx() {
        log.info("Creating Vert.x bean");
        vertx = Vertx.vertx();
        log.info("Creating Vert.x bean done");

        return vertx;
    }

    @PreDestroy
    public void closeVertx() {
        log.info("Closing Vert.x");
        vertx.close(event ->
                log.info("Closing Vert.x done"));
    }
}
