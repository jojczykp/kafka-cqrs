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
import static pl.jojczykp.kafka_cqrs.test_utils.TestUtils.randomProducerDocument
import static pl.jojczykp.kafka_cqrs.test_utils.TestUtils.randomProducerMessage

@WebMvcTest
class ProducerControllerSpec extends Specification {

    @Autowired
    private MockMvc mvc

    @Autowired
    private ProducerMessageAssembler assembler

    def "should create new document"() {
        given:
            ProducerDocument document = randomProducerDocument()
            ProducerMessage message = randomProducerMessage()

        and:
            1 * assembler.toMessage(document) >> message
            1 * sender.send(message)
            0 * _

        expect:
            mvc.perform(MockMvcRequestBuilders
                    .post('/documents')
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonOutput.toJson([
                            id: document.id,
                            author: document.author,
                            text: document.text]))
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(JsonOutput.toJson([
                            id: document.id,
                            author: document.author,
                            text: document.text])))
    }

    @Autowired
    private KafkaSender sender

    @TestConfiguration
    static class MockConfig {
        def detachedMockFactory = new DetachedMockFactory()

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