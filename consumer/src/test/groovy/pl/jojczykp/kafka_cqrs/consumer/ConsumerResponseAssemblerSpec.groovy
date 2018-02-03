package pl.jojczykp.kafka_cqrs.consumer

import spock.lang.Specification

import static pl.jojczykp.kafka_cqrs.consumer.test_utils.TestUtils.randomConsumerDocument

class ConsumerResponseAssemblerSpec extends Specification {

    ConsumerResponseAssembler assembler = new ConsumerResponseAssembler()

    def "should produce response out of document"() {
        given:
            ConsumerDocument document = randomConsumerDocument()

        when:
            ConsumerResponse response = assembler.toResponse(document)

        then:
            response.id == document.id
            response.author == document.author
            response.text == document.text
            response.properties.size() == 4
    }

}
