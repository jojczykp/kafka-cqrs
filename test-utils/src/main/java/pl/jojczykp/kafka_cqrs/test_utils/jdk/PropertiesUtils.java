package pl.jojczykp.kafka_cqrs.test_utils.jdk;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {

    private PropertiesUtils() {}

    public static Properties loadFromResource(String resourceName) {
        InputStream resourceAsStream = getResourceAsStream(resourceName);
        return loadFromStream(resourceAsStream);
    }

    private static InputStream getResourceAsStream(String resourceName) {
        InputStream resourceAsStream = PropertiesUtils.class.getClassLoader().getResourceAsStream(resourceName);
        if (resourceAsStream == null) {
            throw new IllegalArgumentException("Can't find resource " + resourceName);
        }

        return resourceAsStream;
    }

    private static Properties loadFromStream(InputStream resourceAsStream) {
        try {
            Properties result = new Properties();
            result.load(resourceAsStream);
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
