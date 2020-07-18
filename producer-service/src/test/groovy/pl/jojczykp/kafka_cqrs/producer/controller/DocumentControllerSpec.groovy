package pl.jojczykp.kafka_cqrs.producer.controller

import groovy.json.JsonOutput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import pl.jojczykp.kafka_cqrs.producer.assembler.ResponseAssembler
import pl.jojczykp.kafka_cqrs.producer.assembler.MessageAssembler
import pl.jojczykp.kafka_cqrs.producer.message.CreateMessage
import pl.jojczykp.kafka_cqrs.producer.message.DeleteMessage
import pl.jojczykp.kafka_cqrs.producer.message.UpdateMessage
import pl.jojczykp.kafka_cqrs.producer.request.CreateRequest
import pl.jojczykp.kafka_cqrs.producer.request.UpdateRequest
import pl.jojczykp.kafka_cqrs.producer.response.CreateResponse
import pl.jojczykp.kafka_cqrs.producer.service.IdService
import pl.jojczykp.kafka_cqrs.producer.service.SenderService
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import static java.util.UUID.randomUUID
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.jojczykp.kafka_cqrs.producer.test_utils.TestUtils.randomCreateMessage
import static pl.jojczykp.kafka_cqrs.producer.test_utils.TestUtils.randomCreateRequest
import static pl.jojczykp.kafka_cqrs.producer.test_utils.TestUtils.randomDeleteMessage
import static pl.jojczykp.kafka_cqrs.producer.test_utils.TestUtils.randomUpdateMessage
import static pl.jojczykp.kafka_cqrs.producer.test_utils.TestUtils.randomUpdateRequest

@WebMvcTest
class DocumentControllerSpec extends Specification {

    static final String MIME_DOCUMENT_ID = 'application/vnd.kafka-cqrs.document-id.1+json'
    static final String MIME_CREATE_DOCUMENT = 'application/vnd.kafka-cqrs.create-document.1+json'
    static final String MIME_UPDATE_DOCUMENT = 'application/vnd.kafka-cqrs.update-document.1+json'

    @Autowired IdService idGenerator
    @Autowired MessageAssembler messageAssembler
    @Autowired ResponseAssembler responseAssembler
    @Autowired SenderService sender

    @Autowired MockMvc mvc

    def "should create document"() {
        given:
            UUID id = randomUUID()
            CreateRequest request = randomCreateRequest()
            CreateMessage message = randomCreateMessage()
            CreateResponse response = new CreateResponse(id.toString())

        and:
            1 * idGenerator.getRandomId() >> id
            1 * messageAssembler.toMessage(id, request) >> message
            1 * responseAssembler.toResponse(id) >> response
            1 * sender.send(message)
            0 * _

        expect:
            mvc.perform(MockMvcRequestBuilders
                    .post('/documents')
                    .contentType(MIME_CREATE_DOCUMENT)
                    .content(JsonOutput.toJson([
                            author : request.author,
                            text   : request.text])))
                    .andExpect(status().isCreated())
                    .andExpect(header().string('Content-Type', MIME_DOCUMENT_ID))
                    .andExpect(content().json(JsonOutput.toJson([
                            id     : id
                    ])))
    }

    def "should update multiple document fields"() {
        given:
            UpdateRequest request = randomUpdateRequest()
            UpdateMessage message = randomUpdateMessage()

        and:
            1 * messageAssembler.toMessage(request) >> message
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

    def "should update subset of document fields"() {
        given:
            UpdateRequest request = randomUpdateRequest()
            UpdateMessage message = randomUpdateMessage()

        and:
            1 * messageAssembler.toMessage(request) >> message
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

    def "should delete document"() {
        given:
            UUID id = randomUUID()
            DeleteMessage message = randomDeleteMessage()

        and:
            1 * messageAssembler.toMessage(id) >> message
            1 * sender.send(message)
            0 * _

        expect:
            mvc.perform(MockMvcRequestBuilders
                    .delete("/documents/${id}")
                    .content(''))
                    .andExpect(status().isNoContent())
                    .andExpect(content().string(''))
    }

    @TestConfiguration
    static class MockConfig {
        def detachedMockFactory = new DetachedMockFactory()

        @Bean
        IdService idGenerator() {
            return detachedMockFactory.Mock(IdService)
        }

        @Bean
        MessageAssembler messageAssembler() {
            return detachedMockFactory.Mock(MessageAssembler)
        }

        @Bean
        ResponseAssembler responseAssembler() {
            return detachedMockFactory.Mock(ResponseAssembler)
        }

        @Bean
        SenderService sender() {
            return detachedMockFactory.Mock(SenderService)
        }
    }
}