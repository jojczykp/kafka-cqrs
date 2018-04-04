package pl.jojczykp.kafka_cqrs.test_utils;

import org.apache.kafka.common.serialization.Serializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface KafkaTopic {

    String topic();

    Class<? extends Serializer> keySerializer();

    Class<? extends Serializer> valueSerializer();
}
