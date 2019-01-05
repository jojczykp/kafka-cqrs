package pl.jojczykp.kafka_cqrs.e2e_tests

import pl.jojczykp.kafka_cqrs.e2e_tests.clients.NotifierClient
import pl.jojczykp.kafka_cqrs.e2e_tests.clients.ProducerClient
import pl.jojczykp.kafka_cqrs.e2e_tests.clients.ReaderClient
import pl.jojczykp.kafka_cqrs.e2e_tests.responses.SseEvent
import spock.lang.Specification

class CreateReadUpdateDeleteSpec extends Specification {

    private static final String SOME_AUTHOR = 'Author1'
    private static final String SOME_TEXT = 'Some Text'

    def "should Create, Read, Update and Delete document"() {
        given:
            def notifierClient = new NotifierClient()
            def producerClient = new ProducerClient()
            def readerClient = new ReaderClient()

        when:
            def notifierResponse = notifierClient.startListening()

        then:
            notifierResponse.statusCode() == 200
            notifierResponse.headers().allValues('content-type') == ['text/event-stream;charset=UTF-8']
            notifierResponse.nextEvent().fieldsCount() == 0

        when:
            def producerResponse = producerClient.createDocument([
                    'author' : SOME_AUTHOR,
                    'text'   : SOME_TEXT])

        then:
            producerResponse.statusCode() == 201
            producerResponse.headers().allValues('content-type') == ['application/vnd.kafka-cqrs.document-id.1+json;charset=UTF-8']

        and:
            def producerResponseBody = producerResponse.body()
            producerResponseBody.size() == 1

            String id = producerResponseBody.id

        when:
            def readerResponse = readerClient.getDocument(id)

        then:
            readerResponse.statusCode() == 200
            readerResponse.headers().allValues('content-type') == ['application/vnd.kafka-cqrs.document.1+json;charset=UTF-8']

        and:
            def readerResponseBody = readerResponse.body()
            readerResponseBody.id     == id
            readerResponseBody.author == SOME_AUTHOR
            readerResponseBody.text   == SOME_TEXT
            readerResponseBody.size() == 3

        and:
            SseEvent event = notifierResponse.nextEvent()
            event.field('data').header.type == 'CREATE'
            event.field('data').header.id != null
            event.field('data').header.timestamp != null
            event.field('data').header.size() == 3
            event.field('data').payload.id == id
            event.field('data').payload.author == SOME_AUTHOR
            event.field('data').payload.text == SOME_TEXT
            event.field('data').payload.size() == 3
            event.field('data').size() == 2
            event.fieldsCount() == 1
    }
}
