package pl.jojczykp.kafka_cqrs.e2e_tests.clients;

import pl.jojczykp.kafka_cqrs.e2e_tests.responses.JsonResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.net.http.HttpClient.Version.HTTP_1_1;

public class ReaderClient {

    private HttpClient client = HttpClient.newBuilder()
            .version(HTTP_1_1)
            .build();

    public JsonResponse getDocument(String id) {
        try {
            return tryGetDocument(id);
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonResponse tryGetDocument(String id) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create("http://minikube.local/reader/documents/" + id))
                .GET()
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

        return new JsonResponse(response);
    }

}
