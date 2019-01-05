package pl.jojczykp.kafka_cqrs.e2e_tests.clients;

import pl.jojczykp.kafka_cqrs.e2e_tests.responses.JsonResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static groovy.json.JsonOutput.toJson;
import static java.net.http.HttpClient.Version.HTTP_1_1;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

public class ProducerClient {

    private HttpClient client = HttpClient.newBuilder()
            .version(HTTP_1_1)
            .build();

    public JsonResponse createDocument(Map<String, String> document) {
        try {
            return tryCreateDocument(document);
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonResponse tryCreateDocument(Map<String, String> document) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create("http://minikube.local/producer/documents"))
                .header("Content-Type", "application/vnd.kafka-cqrs.create-document.1+json")
                .POST(ofString(toJson(document)))
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

        return new JsonResponse(response);
    }

}
