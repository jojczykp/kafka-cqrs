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

    public Object jsonField(String name) {
        String stringValue = field(name);
        return jsonToMap(stringValue);
    }

    public String field(String name) {
        return fields.get(name);
    }

    public int fieldsCount() {
        return fields.size();
    }
}
