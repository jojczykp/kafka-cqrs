package pl.jojczykp.kafka_cqrs.producer.tools

import pl.jojczykp.kafka_cqrs.producer.messaging.Message
import pl.jojczykp.kafka_cqrs.producer.rest.RequestCreate
import pl.jojczykp.kafka_cqrs.producer.rest.RequestUpdate
import spock.lang.Specification

import java.time.LocalDateTime

import static java.util.UUID.randomUUID
import static pl.jojczykp.kafka_cqrs.producer.test_utils.TestUtils.randomCreateDocumentRequest
import static pl.jojczykp.kafka_cqrs.producer.test_utils.TestUtils.randomUpdateDocumentRequest

class MessageAssemblerSpec extends Specification {

    MessageAssembler assembler = new MessageAssembler()

    def "should produce message out of document create request"() {
        given:
            LocalDateTime before = LocalDateTime.now()
            UUID id = randomUUID()
            RequestCreate request = randomCreateDocumentRequest()

        when:
            Message message = assembler.toMessage(id, request)

        then:
            message.header.messageId != null
            message.header.creationTimestamp >= before
            message.header.creationTimestamp <= LocalDateTime.now()
            message.header.properties.size() == 3

            message.body.id == id
            message.body.author == request.author
            message.body.text == request.text
            message.body.properties.size() == 4

            message.properties.size() == 3
    }

    def "should produce message out of document update request"() {
        given:
            LocalDateTime before = LocalDateTime.now()
            RequestUpdate request = randomUpdateDocumentRequest()

        when:
            Message message = assembler.toMessage(request)

        then:
            message.header.messageId != null
            message.header.creationTimestamp >= before
            message.header.creationTimestamp <= LocalDateTime.now()
            message.header.properties.size() == 3

            message.body.id == request.id
            message.body.author == request.author
            message.body.text == request.text
            message.body.properties.size() == 4

            message.properties.size() == 3
    }
}
