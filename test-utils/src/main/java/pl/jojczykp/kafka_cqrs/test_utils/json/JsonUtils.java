package pl.jojczykp.kafka_cqrs.test_utils.json;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final JavaType MAP_STRING_OBJECT_TYPE = OBJECT_MAPPER.getTypeFactory()
            .constructMapType(HashMap.class, String.class, Object.class);


    private JsonUtils() {}


    public static Map<String, Object> jsonToMap(String bodyText) {
        try {
            return OBJECT_MAPPER.readValue(bodyText, MAP_STRING_OBJECT_TYPE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String mapToJson(Map<String, String> map) {
        try {
            return OBJECT_MAPPER.writeValueAsString(map);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
