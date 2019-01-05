package pl.jojczykp.kafka_cqrs.notifier.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static io.vertx.core.buffer.Buffer.buffer;
import static pl.jojczykp.kafka_cqrs.notifier.config.VertxConfig.MESSAGES_ADDRESS;

@Component
@Slf4j
public class WebNotifierVerticle extends AbstractVerticle {

    @Value("${server.port}")
    private int serverPort;

    private HttpServer server;

    @Override
    public void start() {
        log.info("Starting server on port " + serverPort);

        server = vertx.createHttpServer();
        server.requestHandler(this::handleRequest);
        server.listen(serverPort);

        log.info("Server started");
    }

    private void handleRequest(HttpServerRequest request) {
        log.info("Received client connection");

        HttpServerResponse response = request.response();

        setupHeaders(response);
        MessageConsumer<byte[]> consumer = setupConsumer(response);
        response.closeHandler(e -> handleClientDisconnect(consumer));
        response.write("\n");

        log.info("Client connection registered");
    }

    private void setupHeaders(HttpServerResponse response) {
        response.setChunked(true);
        response.setStatusCode(200);
        response.headers().add("Content-Type", "text/event-stream;charset=UTF-8");
        response.headers().add("Cache-Control", "no-cache");
        response.headers().add("Connection", "keep-alive");
    }

    private MessageConsumer<byte[]> setupConsumer(HttpServerResponse response) {
        log.info("Registering message consumer to event bus");
        MessageConsumer<byte[]> consumer = vertx.eventBus().consumer(MESSAGES_ADDRESS);
        consumer.handler(message -> handleMessage(message, response));
        consumer.completionHandler(e1 ->
                log.info("Registering message consumer to event bus done"));

        return consumer;
    }

    private void handleMessage(Message<byte[]> message, HttpServerResponse response) {
        log.debug("Forwarding message");
        response.write("data: ");
        response.write(buffer(message.body()));
        response.write("\n\n");
    }

    private void handleClientDisconnect(MessageConsumer<byte[]> consumer) {
        log.info("Client disconnected");
        consumer.unregister(e ->
                log.info("Consumer closed"));
    }

    @Override
    public void stop() {
        log.info("Stopping server");
        server.close(e ->
                log.info("Server stopped"));
    }
}
