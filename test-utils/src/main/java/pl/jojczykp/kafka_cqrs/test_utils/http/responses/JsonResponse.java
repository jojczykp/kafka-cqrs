package pl.jojczykp.kafka_cqrs.test_utils.http.responses;

import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import static pl.jojczykp.kafka_cqrs.test_utils.json.JsonUtils.jsonToMap;

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
            return jsonToMap(bodyText);
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
