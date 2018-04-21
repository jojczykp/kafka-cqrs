package pl.jojczykp.kafka_cqrs.test_utils.kafka;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.util.Assert;

public class KafkaTopicContextCustomizer implements ContextCustomizer {

    @Override
    public void customizeContext(ConfigurableApplicationContext context, MergedContextConfiguration mergedConfig) {
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        Assert.isInstanceOf(DefaultSingletonBeanRegistry.class, beanFactory);
        ((AnnotationConfigApplicationContext) context).register(KafkaTopicInjector.class);
    }
}
