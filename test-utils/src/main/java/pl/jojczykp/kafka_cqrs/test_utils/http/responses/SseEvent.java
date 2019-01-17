package pl.jojczykp.kafka_cqrs.test_utils.http.responses;

import java.util.HashMap;
import java.util.Map;

import static pl.jojczykp.kafka_cqrs.test_utils.json.JsonUtils.jsonToMap;

public class SseEvent {

    private Map<String, String> fields = new HashMap<>();

    void addLine(String line) {
        String[] keyValue = line.split(": ", 2);

        String key = keyValue[0];
        String value = keyValue[1];

        fields.compute(key, (k, v) -> (v == null) ? value : v + '\n' + value);
    }

    public Object field(String name) {
        String text = fields.get(name);

        if ("data".equals(name)) {
            return jsonToMap(text);
        } else {
            return text;
        }
    }

    public int fieldsCount() {
        return fields.size();
    }
}
