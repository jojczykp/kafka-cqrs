package pl.jojczykp.kafka_cqrs.persister.config;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import pl.jojczykp.kafka_cqrs.persister.message.Message;

@Configuration
@EnableKafka
@Slf4j
public class KafkaConfig {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.group}")
    private String group;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Message> kafkaListenerContainerFactory() {
        log.info("Creating kafka listener container factory bean");

        ConcurrentKafkaListenerContainerFactory<String, Message> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        log.info("Creating kafka listener container factory bean completed");

        return factory;
    }

    private ConsumerFactory<String, Message> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
            ImmutableMap.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ConsumerConfig.GROUP_ID_CONFIG, group,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MessageJsonDeserializer.class));
    }

    public static class MessageJsonDeserializer extends JsonDeserializer<Message> {}
}
