package pl.jojczykp.awskafka.producer

import spock.lang.Specification

import java.time.LocalDateTime

import static pl.jojczykp.awskafka.test_utils.TestUtils.randomProducerDocument

class ProducerMessageAssemblerTest extends Specification {

    ProducerMessageAssembler assembler = new ProducerMessageAssembler()

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
}
