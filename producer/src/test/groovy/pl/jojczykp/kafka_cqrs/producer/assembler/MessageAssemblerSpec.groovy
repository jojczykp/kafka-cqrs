package pl.jojczykp.kafka_cqrs.producer.assembler

import pl.jojczykp.kafka_cqrs.producer.message.CreateMessage
import pl.jojczykp.kafka_cqrs.producer.message.UpdateMessage
import pl.jojczykp.kafka_cqrs.producer.message.parts.MessageType
import pl.jojczykp.kafka_cqrs.producer.request.CreateDocumentRequest
import pl.jojczykp.kafka_cqrs.producer.request.UpdateDocumentRequest
import spock.lang.Specification

import java.time.LocalDateTime

import static java.util.UUID.randomUUID
import static pl.jojczykp.kafka_cqrs.producer.test_utils.TestUtils.randomCreateRequest
import static pl.jojczykp.kafka_cqrs.producer.test_utils.TestUtils.randomUpdateRequest

class MessageAssemblerSpec extends Specification {

    MessageAssembler assembler = new MessageAssembler()

    def "should produce message out of document create request"() {
        given:
            LocalDateTime before = LocalDateTime.now()
            UUID id = randomUUID()
            CreateDocumentRequest request = randomCreateRequest()

        when:
            CreateMessage message = assembler.toMessage(id, request)

        then:
            message.header.id != null
            message.header.type == MessageType.CREATE
            message.header.creationTimestamp >= before
            message.header.creationTimestamp <= LocalDateTime.now()
            message.header.properties.size() == 4

            message.body.id == id
            message.body.author == request.author
            message.body.text == request.text
            message.body.properties.size() == 4

            message.properties.size() == 3
    }

    def "should produce message out of document update request"() {
        given:
            LocalDateTime before = LocalDateTime.now()
            UpdateDocumentRequest request = randomUpdateRequest()

        when:
            UpdateMessage message = assembler.toMessage(request)

        then:
            message.header.id != null
            message.header.type == MessageType.UPDATE
            message.header.creationTimestamp >= before
            message.header.creationTimestamp <= LocalDateTime.now()
            message.header.properties.size() == 4

            message.body.id == request.id
            message.body.author == request.author
            message.body.text == request.text
            message.body.properties.size() == 4

            message.properties.size() == 3
    }
}
