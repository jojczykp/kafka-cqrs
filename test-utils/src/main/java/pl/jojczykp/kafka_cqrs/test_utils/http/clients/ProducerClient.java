package pl.jojczykp.kafka_cqrs.test_utils.http.clients;

import pl.jojczykp.kafka_cqrs.test_utils.http.responses.JsonResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static java.net.http.HttpClient.Version.HTTP_1_1;
import static java.net.http.HttpRequest.BodyPublishers.ofString;
import static pl.jojczykp.kafka_cqrs.test_utils.json.JsonUtils.mapToJson;

public class ProducerClient {

    private URI documentsUri;

    private HttpClient client = HttpClient.newBuilder()
            .version(HTTP_1_1)
            .build();

    public ProducerClient(String baseUri) {
        this.documentsUri = URI.create(baseUri + "/producer/documents/");
    }

    public JsonResponse createDocument(Map<String, String> document) {
        try {
            return tryCreateDocument(document);
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonResponse tryCreateDocument(Map<String, String> document) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(documentsUri)
                .header("Content-Type", "application/vnd.kafka-cqrs.create-document.1+json")
                .POST(ofString(mapToJson(document)))
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return new JsonResponse(response);
    }

    public JsonResponse updateDocument(Map<String, String> patch) {
        try {
            return tryUpdateDocument(patch);
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonResponse tryUpdateDocument(Map<String, String> patch) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(documentsUri)
                .header("Content-Type", "application/vnd.kafka-cqrs.update-document.1+json")
                .PUT(ofString(mapToJson(patch)))
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return new JsonResponse(response);
    }

    public JsonResponse deleteDocument(String id) {
        try {
            return tryDeleteDocument(id);
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonResponse tryDeleteDocument(String id) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(documentsUri.resolve(id))
                .DELETE()
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return new JsonResponse(response);
    }
}
