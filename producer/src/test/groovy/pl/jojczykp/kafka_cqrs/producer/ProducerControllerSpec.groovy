package pl.jojczykp.kafka_cqrs.producer

import groovy.json.JsonOutput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.jojczykp.kafka_cqrs.test_utils.TestUtils.randomCreateDocumentRequest
import static pl.jojczykp.kafka_cqrs.test_utils.TestUtils.randomCreateDocumentResponse
import static pl.jojczykp.kafka_cqrs.test_utils.TestUtils.randomProducerDocument
import static pl.jojczykp.kafka_cqrs.test_utils.TestUtils.randomCreateDocumentMessage

@WebMvcTest
class ProducerControllerSpec extends Specification {

    @Autowired
    private IdGenerator idGenerator

    @Autowired
    private ProducerMessageAssembler assembler

    @Autowired
    private MockMvc mvc

    def "should create new document"() {
        given:
            UUID id = UUID.randomUUID()
            CreateDocumentRequest request = randomCreateDocumentRequest()
            ProducerDocument document = randomProducerDocument()
            CreateDocumentMessage message = randomCreateDocumentMessage()
            CreateDocumentResponse response = randomCreateDocumentResponse()

        and:
            1 * idGenerator.getRandomId() >> id
            1 * assembler.toModel(id, request) >> document
            1 * assembler.toMessage(document) >> message
            1 * sender.send(message)
            1 * assembler.toResponse(document) >> response
            0 * _

        expect:
            mvc.perform(MockMvcRequestBuilders
                    .post('/documents')
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonOutput.toJson([
                            id     : id,
                            author : request.author,
                            text   : request.text]))
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(JsonOutput.toJson([
                            id     : response.id,
                            author : response.author,
                            text   : response.text])))
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
        ProducerMessageAssembler assembler() {
            return detachedMockFactory.Mock(ProducerMessageAssembler)
        }

        @Bean
        KafkaSender sender() {
            return detachedMockFactory.Mock(KafkaSender)
        }
    }
}