package pl.jojczykp.kafka_cqrs.reader.rest

import spock.lang.Specification

class StringToUUIDConverterSpec extends Specification {

    StringToUUIDConverter converter = new StringToUUIDConverter()

    def "should convert string to UUID"() {
        given:
            UUID uuid = UUID.randomUUID()

        when:
            UUID result = converter.convert(uuid.toString())

        then:
            result == uuid
    }
}
