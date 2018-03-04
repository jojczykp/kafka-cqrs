package pl.jojczykp.kafka_cqrs.notifier.verticle

import io.vertx.core.Vertx
import spock.lang.Specification

import java.util.concurrent.CountDownLatch

import static org.springframework.test.util.ReflectionTestUtils.setField

class WebNotifierVerticleSpec extends Specification {

    static String MESSAGE = '{"some":"message"}'

    int serverPort = getFreePort()

    Vertx vertx
    WebNotifierVerticle verticle = new WebNotifierVerticle()

    def setup() {
        println("Server Port: " + serverPort)

        vertx = Vertx.vertx()

        verticle = new WebNotifierVerticle()
        setField(verticle, 'serverPort', serverPort)

        CountDownLatch deploymentDone = new CountDownLatch(1)
        vertx.deployVerticle(verticle, { deploymentDone.countDown() })
        deploymentDone.await()
    }

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

    static int getFreePort() {
        def socket = new ServerSocket(0)
        int result = socket.getLocalPort()
        socket.close()

        result
    }
}
