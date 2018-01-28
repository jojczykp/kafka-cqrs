package pl.jojczykp.awskafka.producer

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

import static java.util.UUID.randomUUID
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.jojczykp.awskafka.test_utils.TestUtils.randomProducerMessage

@WebMvcTest
class ProducerControllerSpec extends Specification {

    static ProducerMessage MESSAGE = randomProducerMessage()

    @Autowired
    private MockMvc mvc

    @Autowired
    private ProducerMessageAssembler assembler

    @Autowired
    private KafkaSender sender

    def "should handle document creation"() {
        given:
            Map requestBody = [
                    id: randomUUID(),
                    author: 'James Bond',
                    text: 'Once upon a time...']

            ProducerDocument document = ProducerDocument.builder()
                    .id(requestBody.id)
                    .author(requestBody.author)
                    .text(requestBody.text)
                    .build()

        and:
            1 * assembler.toMessage(document) >> MESSAGE
            1 * sender.send(MESSAGE)

        expect:
            mvc.perform(MockMvcRequestBuilders
                    .post("/document")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonOutput.toJson(requestBody))
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(''))
    }

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