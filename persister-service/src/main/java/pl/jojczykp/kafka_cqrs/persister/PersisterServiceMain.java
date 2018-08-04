package pl.jojczykp.kafka_cqrs.persister;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {CassandraDataAutoConfiguration.class})
public class PersisterServiceMain {

    public static void main(String[] args) {
        SpringApplication.run(PersisterServiceMain.class, args);
    }
}
