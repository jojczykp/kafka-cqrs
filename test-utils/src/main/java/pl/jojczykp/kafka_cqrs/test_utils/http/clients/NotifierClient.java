package pl.jojczykp.kafka_cqrs.test_utils.http.clients;

import pl.jojczykp.kafka_cqrs.test_utils.http.responses.SseResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import static java.net.http.HttpClient.Version.HTTP_1_1;
import static java.net.http.HttpResponse.BodyHandlers.ofInputStream;

public class NotifierClient {

    private URI documentsUri;

    private HttpClient client = HttpClient.newBuilder()
            .version(HTTP_1_1)
            .build();

    public NotifierClient(String baseUri) {
        this.documentsUri = URI.create(baseUri + "/notifier/documents/");
    }

    public SseResponse startListening() {
        try {
            return tryStartListening();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private SseResponse tryStartListening() throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(documentsUri)
                .GET()
                .build();

        var response = client.send(request, ofInputStream());

        return new SseResponse(response);
    }

}
