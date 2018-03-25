package pl.jojczykp.kafka_cqrs.persister.service

import org.apache.kafka.common.serialization.StringSerializer
import org.junit.ClassRule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.kafka.config.KafkaListenerEndpointRegistry
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.serializer.JsonSerializer
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource
import pl.jojczykp.kafka_cqrs.persister.config.KafkaConfig
import pl.jojczykp.kafka_cqrs.persister.message.Message
import pl.jojczykp.kafka_cqrs.persister.model.Document
import pl.jojczykp.kafka_cqrs.persister.test_utils.KafkaTemplatedRule
import spock.lang.Shared
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import static org.springframework.kafka.test.utils.ContainerTestUtils.waitForAssignment

@SpringBootTest(classes = [KafkaConfig, MockConfig])
@TestPropertySource(properties = [
        'kafka.bootstrap-servers=${spring.embedded.kafka.brokers}',
        'kafka.group=some-consumer-group',
        'kafka.topic=some.kafka.topic.t'
])
@DirtiesContext
class KafkaListenerServiceSpec extends Specification {

    @ClassRule
    @Shared KafkaTemplatedRule kafka = new KafkaTemplatedRule('some.kafka.topic.t')
    KafkaTemplate template = kafka.createTemplate('some.kafka.topic.t', StringSerializer, JsonSerializer)

    @Autowired
    KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry

    def setup() {
        kafkaListenerEndpointRegistry.listenerContainers.each {
            waitForAssignment(it, kafka.getPartitionsPerTopic())
        }
    }

    @Autowired
    PersistenceService persistenceService

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
