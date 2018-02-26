package pl.jojczykp.kafka_cqrs.persister.service

import com.google.common.collect.ImmutableMap
import pl.jojczykp.kafka_cqrs.persister.message.Message
import pl.jojczykp.kafka_cqrs.persister.model.Document
import pl.jojczykp.kafka_cqrs.persister.repository.DocumentRepository
import spock.lang.Specification

class PersistenceServiceSpec extends Specification {

    DocumentRepository documentRepository = Mock()

    PersistenceService persistenceService = new PersistenceService(repository: documentRepository)

    def "should persist document from message"() {
        given:
            Document document = Document.builder()
                    .id(UUID.randomUUID())
                    .author('Aut Hor')
                    .text('Some text')
                    .build()

            Message message = Message.builder()
                    .header(ImmutableMap.of('header1', 'value1'))
                    .payload(document)
                    .build()
        when:
            persistenceService.onMessage(message)

        then:
            documentRepository.upsertWithDefaultUnset(document)
    }
}
