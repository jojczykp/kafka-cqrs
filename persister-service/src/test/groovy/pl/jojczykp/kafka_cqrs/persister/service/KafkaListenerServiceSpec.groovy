package pl.jojczykp.kafka_cqrs.persister.service

import org.apache.kafka.common.serialization.StringSerializer
import org.junit.ClassRule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.kafka.config.KafkaListenerEndpointRegistry
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer
import org.springframework.kafka.test.rule.KafkaEmbedded
import org.springframework.kafka.test.utils.ContainerTestUtils
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource
import pl.jojczykp.kafka_cqrs.persister.config.KafkaConfig
import pl.jojczykp.kafka_cqrs.persister.message.Message
import pl.jojczykp.kafka_cqrs.persister.model.Document
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
class KafkaListenerServiceSpec extends Specification {

    static final String KAFKA_TOPIC = 'some.kafka.topic.t'

    @Shared @ClassRule
    KafkaEmbedded kafka = new KafkaEmbedded(1, true, KAFKA_TOPIC)

    @Autowired
    KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry

    KafkaTemplate<String, Message> template

    @Autowired
    PersistenceService persistenceService

    def setup() {
        Map senderProperties = KafkaTestUtils.senderProps(kafka.getBrokersAsString())
        senderProperties[KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer
        senderProperties[VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer

        ProducerFactory producerFactory = new DefaultKafkaProducerFactory(senderProperties)

        template = new KafkaTemplate(producerFactory)
        template.setDefaultTopic(KAFKA_TOPIC)

        kafkaListenerEndpointRegistry.listenerContainers.each {
            ContainerTestUtils.waitForAssignment(it, kafka.getPartitionsPerTopic())
        }
    }


    def "should pass message to persister service"() {
        given:
            Message message = Message.builder()
                    .header(['header1': 'value1'])
                    .payload(Document.builder()
                            .id(UUID.randomUUID())
                            .author('Aut Hor')
                            .text('Some text')
                            .build())
                    .build()

        when:
            template.sendDefault(message.payload.id.toString(), message)
            sleep(1000)

        then:
            1 * persistenceService.onMessage(message)
            0 * _
    }


    @TestConfiguration
    static class MockConfig {
        def detachedMockFactory = new DetachedMockFactory()

        @Bean
        KafkaListenerService kafkaListenerService() {
            return new KafkaListenerService()
        }

        @Bean
        PersistenceService persistenceService() {
            return detachedMockFactory.Mock(PersistenceService)
        }
    }
}
