package pl.jojczykp.kafka_cqrs.consumer.rest;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StringToUUIDConverter implements Converter<String, UUID> {

    @Override
    public UUID convert(String string) {
        return UUID.fromString(string);
    }

}
