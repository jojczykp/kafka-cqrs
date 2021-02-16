package pl.jojczykp.kafka_cqrs.e2e_tests.scenarios

import pl.jojczykp.kafka_cqrs.e2e_tests.utils.EnvConfig
import pl.jojczykp.kafka_cqrs.test_utils.http.clients.NotifierClient
import pl.jojczykp.kafka_cqrs.test_utils.http.clients.ProducerClient
import pl.jojczykp.kafka_cqrs.test_utils.http.clients.ReaderClient
import pl.jojczykp.kafka_cqrs.test_utils.http.responses.SseEvent
import spock.lang.Specification

class CreateReadUpdateDeleteSpec extends Specification {

    private static final String SOME_AUTHOR = 'Author1'
    private static final String OTHER_AUTHOR = 'Author2'
    private static final String SOME_TEXT = 'Some Text'

    def "should Create, Read, Update and Delete document"() {
        given:
            def notifierClient = new NotifierClient(EnvConfig.baseUri())
            def producerClient = new ProducerClient(EnvConfig.baseUri())
            def readerClient = new ReaderClient(EnvConfig.baseUri())

        when: 'Start Listening'
            def notifierResponse = notifierClient.startListening()

        then:
            notifierResponse.statusCode() == 200
            notifierResponse.headers().allValues('content-type') == ['text/event-stream;charset=UTF-8']
            notifierResponse.nextEvent().fieldsCount() == 0

        when: 'Create'
            def createResponse = producerClient.createDocument([
                    'author' : SOME_AUTHOR,
                    'text'   : SOME_TEXT])

        then:
            createResponse.statusCode() == 201
            createResponse.headers().allValues('content-type') == ['application/vnd.kafka-cqrs.document-id.1+json']
            createResponse.body().size() == 1

            String id = createResponse.body().id

        and: 'Notify Created'
            SseEvent createEvent = notifierResponse.nextEvent()
            createEvent.jsonField('data').header.type == 'CREATE'
            createEvent.jsonField('data').header.id != null
            createEvent.jsonField('data').header.timestamp != null
            createEvent.jsonField('data').header.size() == 3
            createEvent.jsonField('data').payload.id == id
            createEvent.jsonField('data').payload.author == SOME_AUTHOR
            createEvent.jsonField('data').payload.text == SOME_TEXT
            createEvent.jsonField('data').payload.size() == 3
            createEvent.jsonField('data').size() == 2
            createEvent.fieldsCount() == 1

        when: 'Read Created'
            sleep(1000)
            def readCResponse = readerClient.getDocument(id)

        then:
            readCResponse.statusCode() == 200
            readCResponse.headers().allValues('content-type') == ['application/vnd.kafka-cqrs.document.1+json']
            readCResponse.body().id     == id
            readCResponse.body().author == SOME_AUTHOR
            readCResponse.body().text   == SOME_TEXT
            readCResponse.body().size() == 3

        when: 'Update'
            def updateResponse = producerClient.updateDocument([
                    'id'     : id,
                    'author' : OTHER_AUTHOR])

        then:
            updateResponse.statusCode() == 200
            updateResponse.headers().allValues('content-length') == ['0']
            updateResponse.body().isEmpty()

        and: 'Notify Updated'
            SseEvent updateEvent = notifierResponse.nextEvent()
            updateEvent.jsonField('data').header.type == 'UPDATE'
            updateEvent.jsonField('data').header.id != null
            updateEvent.jsonField('data').header.timestamp != null
            updateEvent.jsonField('data').header.size() == 3
            updateEvent.jsonField('data').payload.id == id
            updateEvent.jsonField('data').payload.author == OTHER_AUTHOR
            updateEvent.jsonField('data').payload.text == null
            updateEvent.jsonField('data').payload.size() == 3
            updateEvent.jsonField('data').size() == 2
            updateEvent.fieldsCount() == 1

        when: 'Read Updated'
            sleep(1000)
            def readUResponse = readerClient.getDocument(id)

        then:
            readUResponse.statusCode() == 200
            readUResponse.headers().allValues('content-type') == ['application/vnd.kafka-cqrs.document.1+json']
            readUResponse.body().id     == id
            readUResponse.body().author == OTHER_AUTHOR
            readUResponse.body().text   == SOME_TEXT
            readUResponse.body().size() == 3

        when: 'Delete'
            def deleteResponse = producerClient.deleteDocument(id)

        then:
            deleteResponse.statusCode() == 204
            deleteResponse.body().isEmpty()

        and: 'Notify Deleted'
            SseEvent deleteEvent = notifierResponse.nextEvent()
            deleteEvent.jsonField('data').header.type == 'DELETE'
            deleteEvent.jsonField('data').header.id != null
            deleteEvent.jsonField('data').header.timestamp != null
            deleteEvent.jsonField('data').header.size() == 3
            deleteEvent.jsonField('data').payload.id == id
            deleteEvent.jsonField('data').payload.author == null
            deleteEvent.jsonField('data').payload.text == null
            deleteEvent.jsonField('data').payload.size() == 3
            deleteEvent.jsonField('data').size() == 2
            deleteEvent.fieldsCount() == 1

        when: 'Fail Read Deleted'
            sleep(1000)
            def readDResponse = readerClient.getDocument(id)

        then:
            readDResponse.statusCode() == 404
            readDResponse.headers().allValues('content-length') == ['0']
    }
}
