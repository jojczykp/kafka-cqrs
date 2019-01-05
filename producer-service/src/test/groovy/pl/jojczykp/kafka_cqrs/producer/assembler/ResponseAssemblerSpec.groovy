package pl.jojczykp.kafka_cqrs.producer.assembler


import pl.jojczykp.kafka_cqrs.producer.response.CreateResponse
import spock.lang.Specification

import static java.util.UUID.randomUUID

class ResponseAssemblerSpec extends Specification {

    ResponseAssembler assembler = new ResponseAssembler()

    def "should produce create document response body out of document id"() {
        given:
            UUID id = randomUUID()

        when:
            CreateResponse response = assembler.toResponse(id)

        then:
            response.id == id.toString()
    }
}
