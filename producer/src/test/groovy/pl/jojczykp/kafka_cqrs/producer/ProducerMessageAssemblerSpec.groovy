package pl.jojczykp.kafka_cqrs.producer

import spock.lang.Specification

import java.time.LocalDateTime

import static java.util.UUID.randomUUID
import static pl.jojczykp.kafka_cqrs.test_utils.TestUtils.randomCreateDocumentRequest
import static pl.jojczykp.kafka_cqrs.test_utils.TestUtils.randomProducerDocument

class ProducerMessageAssemblerSpec extends Specification {

    ProducerMessageAssembler assembler = new ProducerMessageAssembler()

    def "should produce document out of request"() {
        given:
            UUID id = randomUUID()
            CreateDocumentRequest request = randomCreateDocumentRequest()

        when:
            ProducerDocument document = assembler.toModel(id, request)

        then:
            document.id == id
            document.author == request.author
            document.text == request.text
            request.properties.size() == 3
    }

    def "should produce message out of document"() {
        given:
            LocalDateTime before = LocalDateTime.now()
            ProducerDocument document = randomProducerDocument()

        when:
            ProducerMessage message = assembler.toMessage(document)

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
            ProducerDocument document = randomProducerDocument()

        when:
            CreateDocumentResponse response = assembler.toResponse(document)

        then:
            response.id == document.id
            response.author == document.author
            response.text == document.text
            response.properties.size() == 4
    }
}
