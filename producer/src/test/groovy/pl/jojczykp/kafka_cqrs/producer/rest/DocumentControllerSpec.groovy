package pl.jojczykp.kafka_cqrs.producer.rest

import groovy.json.JsonOutput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import pl.jojczykp.kafka_cqrs.producer.assembler.MessageAssembler
import pl.jojczykp.kafka_cqrs.producer.messaging.Sender
import pl.jojczykp.kafka_cqrs.producer.messaging.Message
import pl.jojczykp.kafka_cqrs.producer.tools.IdGenerator
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.jojczykp.kafka_cqrs.producer.test_utils.TestUtils.randomCreateDocumentRequest
import static pl.jojczykp.kafka_cqrs.producer.test_utils.TestUtils.randomDocumentMessage
import static pl.jojczykp.kafka_cqrs.producer.test_utils.TestUtils.randomUpdateDocumentRequest

@WebMvcTest
class DocumentControllerSpec extends Specification {

    public static final String MIME_CREATE_DOCUMENT = 'application/vnd.kafka-cqrs.create-document.1+json'
    public static final String MIME_UPDATE_DOCUMENT = 'application/vnd.kafka-cqrs.update-document.1+json'

    @Autowired IdGenerator idGenerator
    @Autowired MessageAssembler assembler
    @Autowired Sender sender

    @Autowired MockMvc mvc

    def "should create document"() {
        given:
            UUID id = UUID.randomUUID()
            RequestCreate request = randomCreateDocumentRequest()
            Message message = randomDocumentMessage()

        and:
            1 * idGenerator.getRandomId() >> id
            1 * assembler.toMessage(id, request) >> message
            1 * sender.send(message)
            0 * _

        expect:
            mvc.perform(MockMvcRequestBuilders
                    .post('/documents')
                    .contentType(MIME_CREATE_DOCUMENT)
                    .content(JsonOutput.toJson([
                            id     : id,
                            author : request.author,
                            text   : request.text])))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(''))
    }

    def "should update document"() {
        given:
            RequestUpdate request = randomUpdateDocumentRequest()
            Message message = randomDocumentMessage()

        and:
            1 * assembler.toMessage(request) >> message
            1 * sender.send(message)
            0 * _

        expect:
            mvc.perform(MockMvcRequestBuilders
                    .put('/documents')
                    .contentType(MIME_UPDATE_DOCUMENT)
                    .content(JsonOutput.toJson([
                            id     : request.id,
                            author : request.author,
                            text   : request.text])))
                    .andExpect(status().isOk())
                    .andExpect(content().string(''))
    }

    @TestConfiguration
    static class MockConfig {
        def detachedMockFactory = new DetachedMockFactory()

        @Bean
        IdGenerator idGenerator() {
            return detachedMockFactory.Mock(IdGenerator)
        }

        @Bean
        MessageAssembler assembler() {
            return detachedMockFactory.Mock(MessageAssembler)
        }

        @Bean
        Sender sender() {
            return detachedMockFactory.Mock(Sender)
        }
    }
}