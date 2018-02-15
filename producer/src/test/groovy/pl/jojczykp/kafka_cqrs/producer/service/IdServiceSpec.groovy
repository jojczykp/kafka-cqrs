package pl.jojczykp.kafka_cqrs.producer.service

import spock.lang.Specification

class IdServiceSpec extends Specification {

    IdService idGenerator = new IdService()

    def "should generate id"() {
        expect:
            idGenerator.getRandomId() != null
    }
}
