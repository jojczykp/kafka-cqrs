package pl.jojczykp.kafka_cqrs.producer.rest

import groovy.json.JsonOutput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import pl.jojczykp.kafka_cqrs.producer.messaging.CreateDocumentMessage
import pl.jojczykp.kafka_cqrs.producer.messaging.KafkaSender
import pl.jojczykp.kafka_cqrs.producer.model.Document
import pl.jojczykp.kafka_cqrs.producer.tools.IdGenerator
import pl.jojczykp.kafka_cqrs.producer.tools.MessageAssembler
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.jojczykp.kafka_cqrs.producer.test_utils.TestUtils.randomCreateDocumentMessage
import static pl.jojczykp.kafka_cqrs.producer.test_utils.TestUtils.randomCreateDocumentRequest
import static pl.jojczykp.kafka_cqrs.producer.test_utils.TestUtils.randomProducerDocument

@WebMvcTest
class DocumentControllerSpec extends Specification {

    public static final String MIME_CREATE_DOCUMENT = 'application/vnd.kafka-cqrs.create-document.1+json'
    public static final String MIME_ACTUAL_DOCUMENT = 'application/vnd.kafka-cqrs.actual-document.1+json'

    @Autowired
    private IdGenerator idGenerator

    @Autowired
    private MessageAssembler assembler

    @Autowired
    private MockMvc mvc

    def "should create new document"() {
        given:
            UUID id = UUID.randomUUID()
            CreateDocumentRequest request = randomCreateDocumentRequest()
            Document document = randomProducerDocument()
            CreateDocumentMessage message = randomCreateDocumentMessage()

        and:
            1 * idGenerator.getRandomId() >> id
            1 * assembler.toModel(id, request) >> document
            1 * assembler.toMessage(document) >> message
            1 * sender.send(message)
            0 * _

        expect:
            mvc.perform(MockMvcRequestBuilders
                    .post('/documents')
                    .contentType(MIME_CREATE_DOCUMENT)
                    .content(JsonOutput.toJson([
                            id     : id,
                            author : request.author,
                            text   : request.text]))
                    .accept(MIME_ACTUAL_DOCUMENT))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(''))
    }

    @Autowired
    private KafkaSender sender

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
        KafkaSender sender() {
            return detachedMockFactory.Mock(KafkaSender)
        }
    }
}