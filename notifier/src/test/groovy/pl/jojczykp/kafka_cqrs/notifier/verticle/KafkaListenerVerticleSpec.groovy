package pl.jojczykp.kafka_cqrs.notifier.verticle

import io.vertx.core.Vertx
import io.vertx.core.eventbus.EventBus
import spock.lang.Specification

class KafkaListenerVerticleSpec extends Specification {

    static final String MESSAGES_ADDRESS = "messages"

    Vertx vertx = Mock()
    EventBus eventBus = Mock()

    KafkaListenerVerticle kafkaListenerVerticle = new KafkaListenerVerticle(vertx: vertx)

    def "should send message received from kafka to bus"() {
        given:
            byte[] someMessage = [1, 2, 3, 4, 5]

        when:
            kafkaListenerVerticle.onMessage(someMessage)

        then:
            1 * vertx.eventBus() >> eventBus
            1 * eventBus.publish(MESSAGES_ADDRESS, someMessage)
            0 * _
    }
}
