package pl.jojczykp.kafka_cqrs.reader.repository

import org.cassandraunit.spring.CassandraDataSet
import org.cassandraunit.spring.CassandraUnitDependencyInjectionTestExecutionListener
import org.cassandraunit.spring.EmbeddedCassandra
import org.cassandraunit.utils.EmbeddedCassandraServerHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestExecutionListeners
import pl.jojczykp.kafka_cqrs.reader.model.Document
import spock.lang.Specification

import static org.springframework.test.context.TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
import static org.springframework.test.util.ReflectionTestUtils.getField

@TestExecutionListeners(mergeMode = MERGE_WITH_DEFAULTS, listeners = CassandraUnitDependencyInjectionTestExecutionListener)
@EmbeddedCassandra(configuration = "cassandra-test.yaml")
@CassandraDataSet(keyspace = "documents", value = "create_table.cql")
@SpringBootTest(properties = [
        'cassandra.node=#{T(org.cassandraunit.utils.EmbeddedCassandraServerHelper).getHost()}',
        'cassandra.port=#{T(org.cassandraunit.utils.EmbeddedCassandraServerHelper).getNativeTransportPort()}',
        'cassandra.datacenter=datacenter1',
        'cassandra.keyspace=documents'])
class DocumentRepositorySpec extends Specification {

    static { //TODO Remove once migrated to cassandra-unit-4
        getField(EmbeddedCassandraServerHelper.class, 'systemKeyspaces').addAll('system_virtual_schema', 'system_views')
    }

    @Autowired DocumentRepository documentRepository

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
            found.get() == existing
    }

    def "should not find not existing document"() {
        when:
            Optional<Document> found = documentRepository.findById(UUID.randomUUID())

        then:
            !found.present
    }
}
