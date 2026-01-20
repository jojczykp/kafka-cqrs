package pl.jojczykp.kafka_cqrs.reader.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.cassandra.CassandraContainer
import org.testcontainers.spock.Testcontainers
import pl.jojczykp.kafka_cqrs.reader.model.Document
import spock.lang.Shared
import spock.lang.Specification

@Testcontainers
@SpringBootTest
class DocumentRepositorySpec extends Specification {

    @Shared
    static CassandraContainer cassandra = new CassandraContainer("cassandra:4.0")
            .withInitScript("create_table.cql")

    def setupSpec() {
        cassandra.start()
    }

    @DynamicPropertySource
    static void cassandraProperties(DynamicPropertyRegistry registry) {
        registry.add("cassandra.node", cassandra::getHost)
        registry.add("cassandra.port", cassandra::getFirstMappedPort)
        registry.add("cassandra.datacenter") { "datacenter1" }
        registry.add("cassandra.keyspace") { "documents" }
    }

    @Autowired
    DocumentRepository documentRepository

    def "should find existing document"() {
        given:
            Document existing = Document.builder()
                    .id(UUID.randomUUID())
                    .author('Some Author')
                    .text('The Text')
                    .build()

            documentRepository.save(existing)

        when:
            Optional<Document> found = documentRepository.findById(existing.id)

        then:
            found.isPresent()
            found.get() == existing
            found.get().id == existing.id
            found.get().author == 'Some Author'
            found.get().text == 'The Text'
    }

    def "should not find not existing document"() {
        when:
            Optional<Document> found = documentRepository.findById(UUID.randomUUID())

        then:
            !found.isPresent()
    }
}
