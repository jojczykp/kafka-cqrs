package pl.jojczykp.kafka_cqrs.notifier.assembler

import pl.jojczykp.kafka_cqrs.notifier.model.Document
import pl.jojczykp.kafka_cqrs.notifier.rest.ResponseGet
import spock.lang.Specification

import static pl.jojczykp.kafka_cqrs.notifier.test_utils.TestUtils.randomDocument

class ResponseGetAssemblerSpec extends Specification {

    ResponseAssembler assembler = new ResponseAssembler()

    def "should produce response out of document"() {
        given:
            Document document = randomDocument()

        when:
            ResponseGet response = assembler.toResponse(document)

        then:
            response.id == document.id
            response.author == document.author
            response.text == document.text
            response.properties.size() == 4
    }

}
