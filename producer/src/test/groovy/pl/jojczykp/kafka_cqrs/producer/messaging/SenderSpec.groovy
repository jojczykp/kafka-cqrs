package pl.jojczykp.kafka_cqrs.producer.messaging

import org.springframework.kafka.core.KafkaTemplate
import spock.lang.Specification

import static pl.jojczykp.kafka_cqrs.producer.test_utils.TestUtils.randomDocumentMessage

class SenderSpec extends Specification {

    private String topic = 'topic.t'
    private KafkaTemplate kafkaTemplate = Mock()

    private Sender sender = new Sender(topic, kafkaTemplate)

    def "should send message"() {
        given:
            Message message = randomDocumentMessage()

        when:
            sender.send(message)

        then:
            1 * kafkaTemplate.send(topic, message)
            0 * _
    }
}
