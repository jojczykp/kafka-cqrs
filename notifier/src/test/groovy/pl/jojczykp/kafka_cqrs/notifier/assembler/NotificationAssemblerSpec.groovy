package pl.jojczykp.kafka_cqrs.notifier.assembler

import pl.jojczykp.kafka_cqrs.notifier.model.Message
import pl.jojczykp.kafka_cqrs.notifier.rest.Notification
import spock.lang.Specification

import static pl.jojczykp.kafka_cqrs.notifier.test_utils.TestUtils.randomDocument

class NotificationAssemblerSpec extends Specification {

    NotificationAssembler assembler = new NotificationAssembler()

    def "should produce response out of document"() {
        given:
            Message document = randomDocument()

        when:
            Notification response = assembler.toResponse(document)

        then:
            response.id == document.id
            response.author == document.author
            response.text == document.text
            response.properties.size() == 4
    }

}
