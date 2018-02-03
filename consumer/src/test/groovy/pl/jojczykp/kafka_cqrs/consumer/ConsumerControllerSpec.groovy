package pl.jojczykp.kafka_cqrs.consumer

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
import static pl.jojczykp.kafka_cqrs.consumer.test_utils.TestUtils.randomConsumerDocument
import static pl.jojczykp.kafka_cqrs.consumer.test_utils.TestUtils.randomConsumerResponse

@WebMvcTest
class ConsumerControllerSpec extends Specification {

    @Autowired
    private MockMvc mvc

    @Autowired
    private KafkaReader reader

    @Autowired
    private ConsumerResponseAssembler assembler

    def "should return existing document"() {
        given:
            ConsumerDocument document = randomConsumerDocument()
            ConsumerResponse response = randomConsumerResponse()

            1 * reader.find(document.id) >> Optional.of(document)
            1 * assembler.toResponse(document) >> response
            0 * _

        expect:
            mvc.perform(MockMvcRequestBuilders
                    .get("/documents/${document.id}")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(JsonOutput.toJson([
                        'id': response.id,
                        'author': response.author,
                        'text': response.text])))
    }

    def "should return empty to reading not existing document"() {
        given:
            ConsumerDocument document = randomConsumerDocument()

            1 * reader.find(document.id) >> Optional.empty()
            0 * _

        expect:
            mvc.perform(MockMvcRequestBuilders
                    .get("/documents/${document.id}")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string(''))
    }

    @TestConfiguration
    static class MockConfig {
        def detachedMockFactory = new DetachedMockFactory()

        @Bean
        KafkaReader reader() {
            return detachedMockFactory.Mock(KafkaReader)
        }

        @Bean
        ConsumerResponseAssembler assembler() {
            return detachedMockFactory.Mock(ConsumerResponseAssembler)
        }
    }

}
