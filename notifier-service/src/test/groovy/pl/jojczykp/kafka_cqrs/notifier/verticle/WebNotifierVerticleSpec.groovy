package pl.jojczykp.kafka_cqrs.notifier.verticle

import pl.jojczykp.kafka_cqrs.test_utils.http.clients.NotifierClient
import pl.jojczykp.kafka_cqrs.test_utils.http.responses.SseEvent
import pl.jojczykp.kafka_cqrs.test_utils.vertx.TestVertx
import spock.lang.Specification

import static pl.jojczykp.kafka_cqrs.test_utils.tcp.TcpUtils.getFreePort

class WebNotifierVerticleSpec extends Specification {

    static String MESSAGE = '{"some":"message"}'

    def "should forward message to http client"() {
        given:
            def serverPort = getFreePort()
            def verticle = new WebNotifierVerticle(serverPort: serverPort)
            def vertx = TestVertx.with(verticle)
            def client = new NotifierClient("http://localhost:${serverPort}")

        when: 'Start Listening'
            def response = client.startListening()

        then:
            response.statusCode() == 200
            response.headers().allValues('cache-control') == ['no-cache']
            response.headers().allValues('connection') == ['keep-alive']
            response.headers().allValues('content-type') == ['text/event-stream;charset=UTF-8']
            response.headers().allValues('transfer-encoding') == ['chunked']
            response.nextEvent().fieldsCount() == 0

        when: 'Publish Message'
            vertx.eventBus().publish('messages', MESSAGE.bytes)

        then:
            SseEvent event = response.nextEvent()
            event.field('data') == MESSAGE
            event.fieldsCount() == 1

        cleanup:
            vertx.close()
    }
}
