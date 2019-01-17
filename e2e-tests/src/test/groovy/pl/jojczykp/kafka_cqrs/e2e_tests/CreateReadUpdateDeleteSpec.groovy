package pl.jojczykp.kafka_cqrs.e2e_tests


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
            def notifierClient = new NotifierClient()
            def producerClient = new ProducerClient()
            def readerClient = new ReaderClient()

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
            createResponse.headers().allValues('content-type') == ['application/vnd.kafka-cqrs.document-id.1+json;charset=UTF-8']
            createResponse.body().size() == 1

            String id = createResponse.body().id

        and: 'Notify Created'
            SseEvent createEvent = notifierResponse.nextEvent()
            createEvent.field('data').header.type == 'CREATE'
            createEvent.field('data').header.id != null
            createEvent.field('data').header.timestamp != null
            createEvent.field('data').header.size() == 3
            createEvent.field('data').payload.id == id
            createEvent.field('data').payload.author == SOME_AUTHOR
            createEvent.field('data').payload.text == SOME_TEXT
            createEvent.field('data').payload.size() == 3
            createEvent.field('data').size() == 2
            createEvent.fieldsCount() == 1

        when: 'Read Created'
            def readCResponse = readerClient.getDocument(id)

        then:
            readCResponse.statusCode() == 200
            readCResponse.headers().allValues('content-type') == ['application/vnd.kafka-cqrs.document.1+json;charset=UTF-8']
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
            updateEvent.field('data').header.type == 'UPDATE'
            updateEvent.field('data').header.id != null
            updateEvent.field('data').header.timestamp != null
            updateEvent.field('data').header.size() == 3
            updateEvent.field('data').payload.id == id
            updateEvent.field('data').payload.author == OTHER_AUTHOR
            updateEvent.field('data').payload.text == null
            updateEvent.field('data').payload.size() == 3
            updateEvent.field('data').size() == 2
            updateEvent.fieldsCount() == 1

        when: 'Read Updated'
            def readUResponse = readerClient.getDocument(id)

        then:
            readUResponse.statusCode() == 200
            readUResponse.headers().allValues('content-type') == ['application/vnd.kafka-cqrs.document.1+json;charset=UTF-8']
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
            deleteEvent.field('data').header.type == 'DELETE'
            deleteEvent.field('data').header.id != null
            deleteEvent.field('data').header.timestamp != null
            deleteEvent.field('data').header.size() == 3
            deleteEvent.field('data').payload.id == id
            deleteEvent.field('data').payload.author == null
            deleteEvent.field('data').payload.text == null
            deleteEvent.field('data').payload.size() == 3
            deleteEvent.field('data').size() == 2
            deleteEvent.fieldsCount() == 1

        when: 'Fail Read Deleted'
            def readDResponse = readerClient.getDocument(id)

        then:
            readDResponse.statusCode() == 404
            readDResponse.headers().allValues('content-length') == ['0']
    }
}
