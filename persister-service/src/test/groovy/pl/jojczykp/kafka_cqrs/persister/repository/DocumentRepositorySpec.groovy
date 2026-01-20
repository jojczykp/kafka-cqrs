package pl.jojczykp.kafka_cqrs.persister.repository

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.Row
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.cassandra.CassandraContainer
import org.testcontainers.spock.Testcontainers
import pl.jojczykp.kafka_cqrs.persister.model.Document
import spock.lang.Shared
import spock.lang.Specification

@Testcontainers
@SpringBootTest
class DocumentRepositorySpec extends Specification {

    static final String KEYSPACE_NAME = 'documents'
    static final String TABLE_NAME = 'documents'

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
        registry.add("cassandra.keyspace") { KEYSPACE_NAME }
        registry.add("cassandra.table") { TABLE_NAME }
        registry.add("kafka.bootstrap-servers") { "localhost:9092" }
        registry.add("kafka.group") { "test-group" }
        registry.add("kafka.topic") { "test.topic" }
    }

    @Autowired
    DocumentRepository documentRepository

    @Autowired
    ObjectMapper objectMapper

    CqlSession session

    def setup() {
        session = documentRepository.getSession()
    }

    def "should insert new item"() {
        given:
            Document document = Document.builder()
                    .id(UUID.randomUUID())
                    .author('Some Author')
                    .text('Some Text')
                    .build()

        when:
            documentRepository.upsertWithDefaultUnset(document)

        then:
            Row row = select(document.id)
            row.getUuid('id') == document.id
            row.getString('author') == document.author
            row.getString('text') == document.text
            row.columnDefinitions.size() == 3
    }

    def "should update existing item"() {
        given:
            Document original = Document.builder()
                    .id(UUID.randomUUID())
                    .author('Some Author')
                    .text('Some Text')
                    .build()

            Document patch = Document.builder()
                    .id(original.id)
                    .author('new' + original.author)
                    .text(null)
                    .build()

            insert(original)

        when:
            documentRepository.upsertWithDefaultUnset(patch)

        then:
            Row updated = select(original.id)
            updated.getUuid('id') == original.id
            updated.getString('author') == patch.author
            updated.getString('text') == original.text
            updated.columnDefinitions.size() == 3
    }

    def "should delete existing item"() {
        given:
            UUID id = UUID.randomUUID()
            Document original = Document.builder()
                    .id(id)
                    .author('Some Author')
                    .text('Some Text')
                    .build()

            insert(original)

        when:
            documentRepository.delete(id)

        then:
            Row updated = select(original.id)
            updated == null
    }

    private void insert(Document document) {
        session.execute(
                "INSERT INTO ${KEYSPACE_NAME}.${TABLE_NAME} (id, author, text) " +
                        "VALUES (${document.id}, '${document.author}', '${document.text}')")
    }

    private Row select(UUID id) {
        session.execute(
                "SELECT * FROM ${KEYSPACE_NAME}.${TABLE_NAME} WHERE id = ${id}").one()
    }
}
