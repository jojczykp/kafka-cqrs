package pl.jojczykp.kafka_cqrs.persister.test_utils;

import org.apache.kafka.common.serialization.Serializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

public class KafkaTemplatedRule extends KafkaEmbedded {

    public KafkaTemplatedRule(String... topics) {
        super(1, true, topics);
    }

    public <K, V> KafkaTemplate<K, V> createTemplate(String topic, Class<Serializer<K>> keySerializer, Class<Serializer<V>> valueSerializer) {
        Map<String, Object> senderProperties = KafkaTestUtils.senderProps(getBrokersAsString());
        senderProperties.put(KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        senderProperties.put(VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);

        ProducerFactory<K, V> producerFactory = new DefaultKafkaProducerFactory<>(senderProperties);

        KafkaTemplate<K, V> template = new KafkaTemplate<>(producerFactory);
        template.setDefaultTopic(topic);

        return template;
    }
}
