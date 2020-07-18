package pl.jojczykp.kafka_cqrs.reader.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import pl.jojczykp.kafka_cqrs.reader.model.Document;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification.createKeyspace;

@Configuration
@PropertySource("classpath:application.properties")
@EnableCassandraRepositories
public class CassandraConfig extends AbstractCassandraConfiguration {

    @Value("${cassandra.node}")
    private String node;

    @Value("${cassandra.port}")
    private int port;

    @Value("${cassandra.datacenter}")
    private String datacenter;

    @Value("${cassandra.keyspace}")
    private String keyspace;

    @Override
    protected String getContactPoints() {
        return node;
    }

    @Override
    protected int getPort() {
        return port;
    }

    @Override
    protected String getLocalDataCenter() {
        return datacenter;
    }

    @Override
    protected String getKeyspaceName() {
        return keyspace;
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[] { Document.class.getPackage().getName() };
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        return singletonList(createKeyspace(keyspace).ifNotExists());
    }
}
