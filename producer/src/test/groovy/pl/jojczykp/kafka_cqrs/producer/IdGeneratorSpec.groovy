package pl.jojczykp.kafka_cqrs.producer

import spock.lang.Specification

class IdGeneratorSpec extends Specification {

    IdGenerator idGenerator = new IdGenerator()

    def "should generate id"() {
        expect:
            idGenerator.getRandomId() != null
    }
}
