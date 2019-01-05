package pl.jojczykp.kafka_cqrs.e2e_tests.responses;

import groovy.json.JsonSlurper;

import java.io.InputStream;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.util.Map;

public class JsonResponse {

    private int statusCode;
    private HttpHeaders headers;
    private Map<String, Object> body;

    public JsonResponse(HttpResponse<InputStream> response) {
        this.statusCode = response.statusCode();
        this.headers = response.headers();
        this.body = (Map<String, Object>) new JsonSlurper().parse(response.body());
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
