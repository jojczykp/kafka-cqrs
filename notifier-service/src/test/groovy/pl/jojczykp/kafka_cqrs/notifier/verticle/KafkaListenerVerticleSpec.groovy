package pl.jojczykp.kafka_cqrs.notifier.verticle

import io.vertx.core.Vertx
import io.vertx.core.eventbus.EventBus
import org.apache.kafka.common.serialization.ByteArraySerializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource
import pl.jojczykp.kafka_cqrs.notifier.config.KafkaConfig
import pl.jojczykp.kafka_cqrs.test_utils.kafka.KafkaTopic
import spock.lang.Specification
import spock.mock.DetachedMockFactory

@SpringBootTest(classes = [KafkaConfig, MockConfig])
@TestPropertySource(properties = [
        'kafka.bootstrap-servers=${spring.embedded.kafka.brokers}',
        'kafka.group=some-consumer-group',
        'kafka.topic=some.kafka.topic.t'
])
@DirtiesContext
@EmbeddedKafka(topics = '${kafka.topic}')
class KafkaListenerVerticleSpec extends Specification {

    static final String MESSAGES_ADDRESS = 'messages'

    @KafkaTopic(topic = '${kafka.topic}', keySerializer = ByteArraySerializer, valueSerializer = ByteArraySerializer)
    KafkaTemplate template

    @Autowired
    Vertx vertx

    @Autowired
    EventBus eventBus

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
