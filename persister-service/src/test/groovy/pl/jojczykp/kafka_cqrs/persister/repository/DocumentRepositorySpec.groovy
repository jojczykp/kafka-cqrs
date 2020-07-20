package pl.jojczykp.kafka_cqrs.persister.repository

import com.datastax.oss.driver.api.core.cql.Row

import com.fasterxml.jackson.databind.ObjectMapper
import org.cassandraunit.CassandraCQLUnit
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet
import org.junit.Rule
import pl.jojczykp.kafka_cqrs.persister.model.Document
import spock.lang.Specification

class DocumentRepositorySpec extends Specification {

    static final String KEYSPACE_NAME = 'documents'
    static final String TABLE_NAME = 'documents'
    static final long STARTUP_TIMEOUT_MS = 5 * 60 * 60 * 1000;
    static final int READ_TIMEOUT_MS = 1 * 60 * 60 * 1000;

    @Rule CassandraCQLUnit cassandraUnit = new CassandraCQLUnit(
            new ClassPathCQLDataSet("create_table.cql", KEYSPACE_NAME), "cassandra-test.yaml",
                STARTUP_TIMEOUT_MS, READ_TIMEOUT_MS)

    DocumentRepository documentRepository

    def setup() {
        ObjectMapper objectMapper = new ObjectMapper()
        documentRepository = new DocumentRepository(keyspace: KEYSPACE_NAME, table: TABLE_NAME,
                objectMapper: objectMapper, session: cassandraUnit.session)
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
        cassandraUnit.session.execute(
                "INSERT INTO ${KEYSPACE_NAME}.${TABLE_NAME} (id, author, text) " +
                        "VALUES (${document.id}, '${document.author}', '${document.text}')")
    }

    private Row select(UUID id) {
        cassandraUnit.session.execute(
                "SELECT * FROM ${KEYSPACE_NAME}.${TABLE_NAME} WHERE id = ${id}").one()
    }
}
