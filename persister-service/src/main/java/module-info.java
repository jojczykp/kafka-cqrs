module persister.service {
    requires com.datastax.oss.driver.core;
    requires com.fasterxml.jackson.databind;
    requires com.google.common;
    requires java.annotation;
    requires kafka.clients;
    requires lombok;
    requires org.slf4j;
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.data.cassandra;
    requires spring.kafka;
}
