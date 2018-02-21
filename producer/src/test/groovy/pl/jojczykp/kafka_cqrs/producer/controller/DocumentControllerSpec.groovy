package pl.jojczykp.kafka_cqrs.producer.controller

import groovy.json.JsonOutput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import pl.jojczykp.kafka_cqrs.producer.assembler.MessageAssembler
import pl.jojczykp.kafka_cqrs.producer.message.CreateMessage
import pl.jojczykp.kafka_cqrs.producer.message.UpdateMessage
import pl.jojczykp.kafka_cqrs.producer.request.CreateRequest
import pl.jojczykp.kafka_cqrs.producer.request.UpdateRequest
import pl.jojczykp.kafka_cqrs.producer.service.IdService
import pl.jojczykp.kafka_cqrs.producer.service.SenderService
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.jojczykp.kafka_cqrs.producer.test_utils.TestUtils.randomCreateMessage
import static pl.jojczykp.kafka_cqrs.producer.test_utils.TestUtils.randomCreateRequest
import static pl.jojczykp.kafka_cqrs.producer.test_utils.TestUtils.randomUpdateMessage
import static pl.jojczykp.kafka_cqrs.producer.test_utils.TestUtils.randomUpdateRequest

@WebMvcTest
class DocumentControllerSpec extends Specification {

    public static final String MIME_CREATE_DOCUMENT = 'application/vnd.kafka-cqrs.create-document.1+json'
    public static final String MIME_UPDATE_DOCUMENT = 'application/vnd.kafka-cqrs.update-document.1+json'

    @Autowired IdService idGenerator
    @Autowired MessageAssembler assembler
    @Autowired SenderService sender

    @Autowired MockMvc mvc

    def "should create document"() {
        given:
            UUID id = UUID.randomUUID()
            CreateRequest request = randomCreateRequest()
            CreateMessage message = randomCreateMessage()

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
            UpdateRequest request = randomUpdateRequest()
            UpdateMessage message = randomUpdateMessage()

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
        IdService idGenerator() {
            return detachedMockFactory.Mock(IdService)
        }

        @Bean
        MessageAssembler assembler() {
            return detachedMockFactory.Mock(MessageAssembler)
        }

        @Bean
        SenderService sender() {
            return detachedMockFactory.Mock(SenderService)
        }
    }
}