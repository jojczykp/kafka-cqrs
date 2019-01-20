package pl.jojczykp.kafka_cqrs.e2e_tests.utils;

import pl.jojczykp.kafka_cqrs.test_utils.jdk.PropertiesUtils;

import java.util.Properties;

public class EnvConfig {

    private static Properties properties;

    public static String baseUri() {
        return getProperties().getProperty("baseUri");
    }

    private static Properties getProperties() {
        if (properties == null) {
            properties = PropertiesUtils.loadFromResource("environment.properties");
        }

        return properties;
    }
}
