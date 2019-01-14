package pl.jojczykp.kafka_cqrs.e2e_tests.responses;

import groovy.json.JsonSlurper;

import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class JsonResponse {

    private int statusCode;
    private HttpHeaders headers;
    private Map<String, Object> body;

    public JsonResponse(HttpResponse<String> response) {
        this.statusCode = response.statusCode();
        this.headers = response.headers();
        this.body = jsonBodyToMap(response);
    }

    private Map<String, Object> jsonBodyToMap(HttpResponse<String> response) {
        String bodyText = response.body();
        if (bodyText.isEmpty()) {
            return new HashMap<>();
        } else {
            return (Map<String, Object>) new JsonSlurper().parseText(bodyText);
        }
    }

    public int statusCode() {
        return statusCode;
    }

    public HttpHeaders headers() {
        return headers;
    }

    public Map<String, Object> body() {
        return body;
    }
}
