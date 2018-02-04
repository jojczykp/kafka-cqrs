package pl.jojczykp.kafka_cqrs.producer.tools

import pl.jojczykp.kafka_cqrs.producer.messaging.CreateDocumentMessage
import pl.jojczykp.kafka_cqrs.producer.model.Document
import pl.jojczykp.kafka_cqrs.producer.rest.CreateDocumentRequest
import pl.jojczykp.kafka_cqrs.producer.rest.CreateDocumentResponse
import spock.lang.Specification

import java.time.LocalDateTime

import static java.util.UUID.randomUUID
import static pl.jojczykp.kafka_cqrs.producer.test_utils.TestUtils.randomCreateDocumentRequest
import static pl.jojczykp.kafka_cqrs.producer.test_utils.TestUtils.randomProducerDocument

class CreateDocumentMessageAssemblerSpec extends Specification {

    MessageAssembler assembler = new MessageAssembler()

    def "should produce document out of request"() {
        given:
            UUID id = randomUUID()
            CreateDocumentRequest request = randomCreateDocumentRequest()

        when:
            Document document = assembler.toModel(id, request)

        then:
            document.id == id
            document.author == request.author
            document.text == request.text
            request.properties.size() == 3
    }

    def "should produce message out of document"() {
        given:
            LocalDateTime before = LocalDateTime.now()
            Document document = randomProducerDocument()

        when:
            CreateDocumentMessage message = assembler.toMessage(document)

        then:
            message.header.messageId != null
            message.header.creationTimestamp >= before
            message.header.creationTimestamp <= LocalDateTime.now()
            message.header.properties.size() == 3

            message.body.id == document.id
            message.body.author == document.author
            message.body.text == document.text
            message.body.properties.size() == 4

            message.properties.size() == 3
    }

    def "should produce response out of document"() {
        given:
            Document document = randomProducerDocument()

        when:
            CreateDocumentResponse response = assembler.toResponse(document)

        then:
            response.id == document.id
            response.author == document.author
            response.text == document.text
            response.properties.size() == 4
    }
}
