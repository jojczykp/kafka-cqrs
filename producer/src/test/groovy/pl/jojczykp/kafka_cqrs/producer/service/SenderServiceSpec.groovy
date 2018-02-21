package pl.jojczykp.kafka_cqrs.producer.service

import org.springframework.kafka.core.KafkaTemplate
import pl.jojczykp.kafka_cqrs.producer.message.parts.Message
import spock.lang.Specification

import static pl.jojczykp.kafka_cqrs.producer.test_utils.TestUtils.randomCreateMessage

class SenderServiceSpec extends Specification {

    private String topic = 'topic.t'
    private KafkaTemplate kafkaTemplate = Mock()

    private SenderService sender = new SenderService(topic, kafkaTemplate)

    def "should send message"() {
        given:
            Message message = randomCreateMessage()

        when:
            sender.send(message)

        then:
            1 * kafkaTemplate.send(topic, message.body.id.toString(), message)
            0 * _
    }
}
