package pl.jojczykp.kafka_cqrs.test_utils.kafka;

import org.apache.kafka.common.serialization.ByteArraySerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface KafkaTopic {

    String topic();

    Class<? extends ByteArraySerializer> keySerializer();

    Class<? extends ByteArraySerializer> valueSerializer();
}
