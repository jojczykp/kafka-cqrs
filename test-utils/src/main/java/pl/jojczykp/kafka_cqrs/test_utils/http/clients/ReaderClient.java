package pl.jojczykp.kafka_cqrs.test_utils.http.clients;

import pl.jojczykp.kafka_cqrs.test_utils.http.responses.JsonResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.net.http.HttpClient.Version.HTTP_2;

public class ReaderClient {

    private URI documentsUri;

    private HttpClient client = HttpClient.newBuilder()
            .version(HTTP_2)
            .build();

    public ReaderClient(String baseUri) {
        this.documentsUri = URI.create(baseUri + "/reader/documents/");
    }

    public JsonResponse getDocument(String id) {
        try {
            return tryGetDocument(id);
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonResponse tryGetDocument(String id) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(documentsUri.resolve(id))
                .GET()
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return new JsonResponse(response);
    }
}
