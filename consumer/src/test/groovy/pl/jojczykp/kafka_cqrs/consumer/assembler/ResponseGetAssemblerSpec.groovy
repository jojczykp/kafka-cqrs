package pl.jojczykp.kafka_cqrs.consumer.assembler

import pl.jojczykp.kafka_cqrs.consumer.model.KafkaDocument
import pl.jojczykp.kafka_cqrs.consumer.rest.ResponseGet
import spock.lang.Specification

import static pl.jojczykp.kafka_cqrs.consumer.test_utils.TestUtils.randomConsumerDocument

class ResponseGetAssemblerSpec extends Specification {

    ResponseAssembler assembler = new ResponseAssembler()

    def "should produce response out of document"() {
        given:
            KafkaDocument document = randomConsumerDocument()

        when:
            ResponseGet response = assembler.toResponse(document)

        then:
            response.id == document.id
            response.author == document.author
            response.text == document.text
            response.properties.size() == 4
    }

}
