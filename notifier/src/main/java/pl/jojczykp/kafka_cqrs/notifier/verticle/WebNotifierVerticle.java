package pl.jojczykp.kafka_cqrs.notifier.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static io.vertx.core.buffer.Buffer.buffer;
import static pl.jojczykp.kafka_cqrs.notifier.config.VertxConfig.MESSAGES_ADDRESS;

@Component
public class WebNotifierVerticle extends AbstractVerticle {

    @Value("${server.port}")
    private int serverPort;

    private HttpServer server;

    @Override
    public void start() {
        System.out.println("Starting server");

        server = vertx.createHttpServer();
        server.requestHandler(this::handleRequest);
        server.listen(serverPort);

        System.out.println("Server started, listening on " + serverPort);
    }

    private void handleRequest(HttpServerRequest request) {
        HttpServerResponse response = request.response();

        setupHeaders(response);

        System.out.println("Registering consumer");
        MessageConsumer<byte[]> consumer = vertx.eventBus().consumer(MESSAGES_ADDRESS);
        consumer.handler(message -> handleMessage(message, response));
        consumer.completionHandler(e1 ->
                System.out.println("Registering consumer - done"));

        response.closeHandler(e -> handleClientDisconnect(consumer));

        response.write("");
    }

    private void setupHeaders(HttpServerResponse response) {
        response.setChunked(true);
        response.setStatusCode(200);
        response.headers().add("Content-Type", "text/event-stream;charset=UTF-8");
        response.headers().add("Cache-Control", "no-cache");
        response.headers().add("Connection", "keep-alive");
    }

    private void handleMessage(Message<byte[]> message, HttpServerResponse response) {
        System.out.println("forwarding");
        response.write("data: ");
        response.write(buffer(message.body()));
        response.write("\n\n");
    }

    private void handleClientDisconnect(MessageConsumer<byte[]> consumer) {
        System.out.println("client disconnected");
        consumer.unregister(e ->
                System.out.println("close handler"));
    }

    @Override
    public void stop() {
        System.out.println("Stopping server");
        server.close(e ->
                System.out.println("Server stopped"));
    }
}
