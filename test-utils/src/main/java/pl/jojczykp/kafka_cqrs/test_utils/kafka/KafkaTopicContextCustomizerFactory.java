package pl.jojczykp.kafka_cqrs.test_utils.kafka;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;

import java.util.List;

public class KafkaTopicContextCustomizerFactory implements ContextCustomizerFactory {

    @Override
    public ContextCustomizer createContextCustomizer(Class<?> testClass,
                                                     List<ContextConfigurationAttributes> configAttributes) {
        EmbeddedKafka kafkaTopics = AnnotatedElementUtils.findMergedAnnotation(testClass, EmbeddedKafka.class);
        return kafkaTopics != null ? new KafkaTopicContextCustomizer() : null;
    }
}
