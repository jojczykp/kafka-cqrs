package pl.jojczykp.kafka_cqrs.test_utils;

import org.apache.kafka.common.serialization.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.env.PropertyResolver;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.rule.KafkaRule;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;
import static org.springframework.kafka.test.utils.ContainerTestUtils.waitForAssignment;

@Component
public class KafkaTemplateInjector implements BeanPostProcessor {

    @Autowired
    private PropertyResolver propertyResolver;

    @Autowired
    private KafkaRule kafkaRule;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        stream(bean.getClass().getDeclaredFields())
                .filter(f -> f.getAnnotation(KafkaTopic.class) != null)
                .forEach(f -> injectTemplate(bean, f));

        return bean;
    }

    private <K, V> void injectTemplate(Object bean, Field field) {
        KafkaTopic annotation = field.getAnnotation(KafkaTopic.class);
        String topic = propertyResolver.resolvePlaceholders(annotation.topic());
        Class<Serializer<K>> keySerializer = (Class<Serializer<K>>) annotation.keySerializer();
        Class<Serializer<V>> valueSerializer = (Class<Serializer<V>>) annotation.valueSerializer();

        KafkaTemplate<K, V> template = createTemplate(topic, keySerializer, valueSerializer);

        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, bean, template);
    }

    public <K, V> KafkaTemplate<K, V> createTemplate(String topic, Class<Serializer<K>> keySerializer, Class<Serializer<V>> valueSerializer) {
        Map<String, Object> senderProperties = KafkaTestUtils.senderProps(kafkaRule.getBrokersAsString());
        senderProperties.put(KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        senderProperties.put(VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);

        ProducerFactory<K, V> producerFactory = new DefaultKafkaProducerFactory<>(senderProperties);

        KafkaTemplate<K, V> template = new KafkaTemplate<>(producerFactory);
        template.setDefaultTopic(topic);

        for (MessageListenerContainer c: kafkaListenerEndpointRegistry.getListenerContainers()) {
            try {
                waitForAssignment(c, kafkaRule.getPartitionsPerTopic());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return template;
    }
}
