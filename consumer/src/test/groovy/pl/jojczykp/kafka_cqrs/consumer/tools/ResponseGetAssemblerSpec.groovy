package pl.jojczykp.kafka_cqrs.consumer.tools

import pl.jojczykp.kafka_cqrs.consumer.model.Document
import pl.jojczykp.kafka_cqrs.consumer.rest.ResponseGet
import spock.lang.Specification

import static pl.jojczykp.kafka_cqrs.consumer.test_utils.TestUtils.randomConsumerDocument

class ResponseGetAssemblerSpec extends Specification {

    ConsumerResponseAssembler assembler = new ConsumerResponseAssembler()

    def "should produce response out of document"() {
        given:
            Document document = randomConsumerDocument()

        when:
            ResponseGet response = assembler.toResponse(document)

        then:
            response.id == document.id
            response.author == document.author
            response.text == document.text
            response.properties.size() == 4
    }

}
