package pl.jojczykp.kafka_cqrs.notifier.verticle

import pl.jojczykp.kafka_cqrs.test_utils.http.HttpReader
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
            def client = HttpReader.connect("http://localhost:${serverPort}")

        when:
            vertx.eventBus().publish('messages', MESSAGE.bytes)

        then:
            client.getResponseCode() == 200

        and:
            Map headers = client.getHeaderFields()
            headers.get(null) == ['HTTP/1.1 200 OK']
            headers.get('transfer-encoding') == ['chunked']
            headers.get('Cache-Control') == ['no-cache']
            headers.get('Connection') == ['keep-alive']
            headers.get('Content-Type') == ['text/event-stream;charset=UTF-8']
            headers.size() == 5

        and:
            client.readLine() == 'data: ' + MESSAGE
            client.readLine() == ''

        cleanup:
            close(client)
            close(vertx)
    }

    def close(closeable) {
        closeable && closeable.close()
    }
}
