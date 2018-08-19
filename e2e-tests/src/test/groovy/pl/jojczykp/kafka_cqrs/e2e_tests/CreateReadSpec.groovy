package pl.jojczykp.kafka_cqrs.e2e_tests

import groovyx.net.http.RESTClient
import spock.lang.Specification


class CreateReadSpec extends Specification {

    def "should read created document"() {
        given:
            def client = new RESTClient('http://minikube.local')
            client.encoder['application/vnd.kafka-cqrs.create-document.1+json'] = client.encoder['application/json']

        when:
            def response = client.post(
                    path: '/producer/documents',
                    contentType: 'application/vnd.kafka-cqrs.create-document.1+json',
                    headers: [
                            'Content-Type': 'application/vnd.kafka-cqrs.create-document.1+json'
                    ],
                    body: [
                            'author':'Author1',
                            'text':'Some Text'
                    ]
            )

        then:
            response.statusLine.statusCode == 201
    }
}
