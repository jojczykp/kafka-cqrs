package pl.jojczykp.kafka_cqrs.notifier.verticle

import io.vertx.core.Vertx
import io.vertx.core.eventbus.EventBus
import org.apache.kafka.common.serialization.ByteArraySerializer
import org.junit.ClassRule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.kafka.config.KafkaListenerEndpointRegistry
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.test.rule.KafkaEmbedded
import org.springframework.kafka.test.utils.ContainerTestUtils
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource
import pl.jojczykp.kafka_cqrs.notifier.config.KafkaConfig
import spock.lang.Shared
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG

@SpringBootTest(classes = [KafkaConfig, MockConfig])
@TestPropertySource(properties = [
        'kafka.bootstrap-servers=${spring.embedded.kafka.brokers}',
        'kafka.group=some-consumer-group',
        'kafka.topic=some.kafka.topic.t'
])
@DirtiesContext
class KafkaListenerVerticleSpec extends Specification {

    static final String MESSAGES_ADDRESS = 'messages'

    static final String KAFKA_TOPIC = 'some.kafka.topic.t'

    @Shared @ClassRule
    KafkaEmbedded kafka = new KafkaEmbedded(1, true, KAFKA_TOPIC)

    @Autowired
    KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry

    KafkaTemplate<byte[], byte[]> template

    @Autowired
    Vertx vertx

    @Autowired
    EventBus eventBus

    def setup() {
        Map senderProperties = KafkaTestUtils.senderProps(kafka.getBrokersAsString())
        senderProperties[KEY_SERIALIZER_CLASS_CONFIG] = ByteArraySerializer
        senderProperties[VALUE_SERIALIZER_CLASS_CONFIG] = ByteArraySerializer

        ProducerFactory producerFactory = new DefaultKafkaProducerFactory(senderProperties)

        template = new KafkaTemplate(producerFactory)
        template.setDefaultTopic(KAFKA_TOPIC)

        kafkaListenerEndpointRegistry.listenerContainers.each {
            ContainerTestUtils.waitForAssignment(it, kafka.getPartitionsPerTopic())
        }
    }


    def "should send message received from kafka to bus"() {
        given:
            byte[] key = [1, 2, 3, 4, 5]
            byte[] message = [6, 7, 8, 9, 0]

        when:
            template.sendDefault(key, message)
            sleep(1000)

        then:
            1 * vertx.eventBus() >> eventBus
            1 * eventBus.publish(MESSAGES_ADDRESS, message)
            0 * _
    }


    @TestConfiguration
    static class MockConfig {
        def detachedMockFactory = new DetachedMockFactory()

        @Bean
        KafkaListenerVerticle kafkaListenerVerticle() {
            return new KafkaListenerVerticle()
        }

        @Bean
        Vertx vertx() {
            return detachedMockFactory.Mock(Vertx)
        }

        @Bean
        EventBus eventBus() {
            return detachedMockFactory.Mock(EventBus)
        }
    }
}
