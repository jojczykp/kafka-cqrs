package pl.jojczykp.kafka_cqrs.notifier.verticle

import io.vertx.core.Vertx
import pl.jojczykp.kafka_cqrs.test_utils.vertx.TestVertx
import spock.lang.Specification

import static pl.jojczykp.kafka_cqrs.test_utils.tcp.TcpUtils.getFreePort

class WebNotifierVerticleSpec extends Specification {

    static String MESSAGE = '{"some":"message"}'

    int serverPort = getFreePort()
    Vertx vertx = TestVertx.with(
            new WebNotifierVerticle(serverPort: serverPort)
    )

    def "should forward message to http client"() {
        given:
            HttpReader client = HttpReader.connect("http://localhost:${serverPort}")

        when:
            vertx.eventBus().publish('messages', MESSAGE.bytes)

        then:
            client.getResponseCode() == 200

        and:
            Map headers = client.getHeaderFields()
            headers.get(null) == ['HTTP/1.1 200 OK']
            headers.get('Transfer-Encoding') == ['chunked']
            headers.get('Cache-Control') == ['no-cache']
            headers.get('Connection') == ['keep-alive']
            headers.get('Content-Type') == ['text/event-stream;charset=UTF-8']
            headers.size() == 5

        and:
            client.readLine() == 'data: ' + MESSAGE
            client.readLine() == ''
            client.readLine() == null

        cleanup:
            client.close()
    }

    def cleanup() {
        vertx.close()
    }
}
