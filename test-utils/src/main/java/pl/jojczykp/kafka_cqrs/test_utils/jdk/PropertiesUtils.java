package pl.jojczykp.kafka_cqrs.test_utils.jdk;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertiesUtils {

    private static final Pattern ENV_VAR_PATTERN = Pattern.compile("\\$\\{env:(.*)}");

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

    public static void resolveEnvVariables(Properties original) {
        original.replaceAll((key, oldVal) -> resolveEnvVariables((String) oldVal));
    }

    private static String resolveEnvVariables(String oldVal) {
        Matcher matcher = ENV_VAR_PATTERN.matcher(oldVal);
        return matcher.replaceAll(matchResult -> System.getenv(matchResult.group(1)));
    }
}
