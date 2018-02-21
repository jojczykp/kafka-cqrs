package pl.jojczykp.kafka_cqrs.reader.controller

import groovy.json.JsonOutput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import pl.jojczykp.kafka_cqrs.reader.entity.Document
import pl.jojczykp.kafka_cqrs.reader.repository.DocumentRepository
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import static java.util.UUID.randomUUID
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
class ControllerSpec extends Specification {

    public static final String MIME_DOCUMENT = 'application/vnd.kafka-cqrs.document.1+json'

    @Autowired
    private MockMvc mvc

    @Autowired
    private DocumentRepository reader

    def "should return existing document"() {
        given:
            Document document = randomDocument()

            1 * reader.find(document.id) >> Optional.of(document)
            0 * _

        expect:
            mvc.perform(MockMvcRequestBuilders
                    .get("/documents/${document.id}")
                    .accept(MIME_DOCUMENT))
                    .andExpect(status().isOk())
                    .andExpect(content().json(JsonOutput.toJson([
                        id     : document.id,
                        author : document.author,
                        text   : document.text])))
    }

    def "should return empty reading not existing document"() {
        given:
            Document document = randomDocument()

            1 * reader.find(document.id) >> Optional.empty()
            0 * _

        expect:
            mvc.perform(MockMvcRequestBuilders
                    .get("/documents/${document.id}")
                    .accept(MIME_DOCUMENT))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string(''))
    }

    static Document randomDocument() {
        return Document.builder()
                .id(randomUUID())
                .author(randomAlphabetic(10))
                .text(randomAlphanumeric(10, 50))
                .build()
    }

    @TestConfiguration
    static class MockConfig {
        def detachedMockFactory = new DetachedMockFactory()

        @Bean
        DocumentRepository reader() {
            return detachedMockFactory.Mock(DocumentRepository)
        }
    }

}
