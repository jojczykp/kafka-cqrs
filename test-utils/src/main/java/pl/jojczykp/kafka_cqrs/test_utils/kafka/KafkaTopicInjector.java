package pl.jojczykp.kafka_cqrs.test_utils.kafka;

import org.apache.kafka.common.serialization.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.env.PropertyResolver;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

@Component
public class KafkaTopicInjector implements BeanPostProcessor {

    @Autowired
    private PropertyResolver propertyResolver;

    @Autowired
    private EmbeddedKafkaBroker kafkaBroker;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        stream(bean.getClass().getDeclaredFields())
                .filter(f -> f.getAnnotation(KafkaTopic.class) != null)
                .forEach(f -> injectKafkaTemplate(bean, f));

        return bean;
    }

    private <K extends Serializer, V extends Serializer> void injectKafkaTemplate(Object bean, Field field) {
        KafkaTopic annotation = field.getAnnotation(KafkaTopic.class);

        String topic = propertyResolver.resolvePlaceholders(annotation.topic());
        Class<? extends Serializer> keySerializer = annotation.keySerializer();
        Class<? extends Serializer> valueSerializer = annotation.valueSerializer();

        KafkaTemplate<K, V> template = createTemplate(topic, keySerializer, valueSerializer);

        setField(bean, field, template);
    }

    private <K, V> KafkaTemplate<K, V> createTemplate(String topic,
                                                      Class<? extends Serializer> keySerializer,
                                                      Class<? extends Serializer> valueSerializer) {
        ProducerFactory<K, V> producerFactory = createProducerFactory(keySerializer, valueSerializer);
        KafkaTemplate<K, V> template = new KafkaTemplate<>(producerFactory);

        template.setDefaultTopic(topic);

        waitForAllAssignments();

        return template;
    }

    private <K, V> ProducerFactory<K, V> createProducerFactory(Class<? extends Serializer> keySerializer,
                                                               Class<? extends Serializer> valueSerializer) {
        Map<String, Object> senderProperties = KafkaTestUtils.senderProps(kafkaBroker.getBrokersAsString());
        senderProperties.put(KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        senderProperties.put(VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);

        return new DefaultKafkaProducerFactory<>(senderProperties);
    }

    private void waitForAllAssignments() {
        kafkaListenerEndpointRegistry.getListenerContainers().forEach(c -> {
            try {
                ContainerTestUtils.waitForAssignment(c, kafkaBroker.getPartitionsPerTopic());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private <K extends Serializer, V extends Serializer> void setField(Object bean, Field field,
                                                                       KafkaTemplate<K, V> template) {
        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, bean, template);
    }
}
