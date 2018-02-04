package pl.jojczykp.kafka_cqrs.reader.assembler

import pl.jojczykp.kafka_cqrs.reader.model.Document
import pl.jojczykp.kafka_cqrs.reader.rest.ResponseGet
import spock.lang.Specification

import static pl.jojczykp.kafka_cqrs.reader.test_utils.TestUtils.randomDocument

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
