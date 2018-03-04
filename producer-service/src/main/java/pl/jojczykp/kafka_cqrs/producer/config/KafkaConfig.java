package pl.jojczykp.kafka_cqrs.producer.config;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import pl.jojczykp.kafka_cqrs.producer.message.parts.Message;

@Configuration
@Slf4j
public class KafkaConfig {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public KafkaTemplate<String, Message> kafkaTemplate() {
        log.info("Creating kafka template bean");
        KafkaTemplate<String, Message> bean = new KafkaTemplate<>(producerFactory());
        log.info("Creating kafka template bean done");

        return bean;
    }

    private ProducerFactory<String, Message> producerFactory() {
        return new DefaultKafkaProducerFactory<>(ImmutableMap.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, NoTypeInfoJsonSerializer.class));
    }

    public static class NoTypeInfoJsonSerializer extends JsonSerializer {
        public NoTypeInfoJsonSerializer() {
            setAddTypeInfo(false);
        }
    }
}
