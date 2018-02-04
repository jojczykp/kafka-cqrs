package pl.jojczykp.kafka_cqrs.notifier.rest

import groovy.json.JsonOutput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import pl.jojczykp.kafka_cqrs.notifier.messaging.Listener
import pl.jojczykp.kafka_cqrs.notifier.model.Message
import pl.jojczykp.kafka_cqrs.notifier.assembler.NotificationAssembler
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.jojczykp.kafka_cqrs.notifier.test_utils.TestUtils.randomDocument
import static pl.jojczykp.kafka_cqrs.notifier.test_utils.TestUtils.randomResponse

@WebMvcTest
class MessageControllerSpec extends Specification {

    public static final String MIME_DOCUMENT = 'application/vnd.kafka-cqrs.document.1+json'

    @Autowired
    private MockMvc mvc

    @Autowired
    private Listener reader

    @Autowired
    private NotificationAssembler assembler

    def "should return existing document"() {
        given:
            Message document = randomDocument()
            Notification response = randomResponse()

            1 * reader.find(document.id) >> Optional.of(document)
            1 * assembler.toResponse(document) >> response
            0 * _

        expect:
            mvc.perform(MockMvcRequestBuilders
                    .get("/documents/${document.id}")
                    .accept(MIME_DOCUMENT))
                    .andExpect(status().isOk())
                    .andExpect(content().json(JsonOutput.toJson([
                        id     : response.id,
                        author : response.author,
                        text   : response.text])))
    }

    def "should return empty reading not existing document"() {
        given:
            Message document = randomDocument()

            1 * reader.find(document.id) >> Optional.empty()
            0 * _

        expect:
            mvc.perform(MockMvcRequestBuilders
                    .get("/documents/${document.id}")
                    .accept(MIME_DOCUMENT))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string(''))
    }

    @TestConfiguration
    static class MockConfig {
        def detachedMockFactory = new DetachedMockFactory()

        @Bean
        Listener reader() {
            return detachedMockFactory.Mock(Listener)
        }

        @Bean
        NotificationAssembler assembler() {
            return detachedMockFactory.Mock(NotificationAssembler)
        }
    }

}
