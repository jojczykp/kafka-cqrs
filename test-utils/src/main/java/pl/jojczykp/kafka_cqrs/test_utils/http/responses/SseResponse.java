package pl.jojczykp.kafka_cqrs.test_utils.http.responses;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;

public class SseResponse {

    private int statusCode;
    private HttpHeaders headers;
    private BufferedReader bodyReader;

    public SseResponse(HttpResponse<InputStream> response) {
        this.statusCode = response.statusCode();
        this.headers = response.headers();
        this.bodyReader = new BufferedReader(new InputStreamReader(response.body()));
    }

    public int statusCode() {
        return statusCode;
    }

    public HttpHeaders headers() {
        return headers;
    }

    public SseEvent nextEvent() {
        try {
            return tryNextEvent();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private SseEvent tryNextEvent() throws IOException {
        SseEvent event = new SseEvent();

        while (true) {
            String line = bodyReader.readLine();
            if (line.isEmpty()) {
                break;
            }

            event.addLine(line);
        }

        return event;
    }
}
