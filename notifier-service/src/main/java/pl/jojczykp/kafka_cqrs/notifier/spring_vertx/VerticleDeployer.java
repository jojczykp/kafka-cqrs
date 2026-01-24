package pl.jojczykp.kafka_cqrs.notifier.spring_vertx;

import io.vertx.core.AsyncResult;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class VerticleDeployer implements BeanPostProcessor {

    private final Vertx vertx;

    public VerticleDeployer(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName) {
        if (bean instanceof Verticle) {
            deployVerticle((Verticle) bean, beanName);
        }

        return bean;
    }

    private void deployVerticle(Verticle verticle, String beanName) {
        log.info("Deploying bean {} of type {}", beanName, verticle.getClass().getName());
        vertx.deployVerticle(verticle)
             .onComplete(result ->
                     handleResult(beanName, verticle.getClass().getName(), result));
    }

    private void handleResult(String beanName, String typeName, AsyncResult<String> result) {
        if (result.failed()) {
            handleFailure(typeName, beanName, result);
        } else {
            handleSuccess(typeName, beanName, result);
        }
    }

    private void handleFailure(String typeName, String beanName, AsyncResult<String> result) {
        String msg = String.format("Verticle bean %s of type %s deployment failed: %s",
                beanName, typeName, result.cause());

        log.error(msg);

        throw new BeanInitializationException(msg, result.cause());
    }

    private void handleSuccess(String typeName, String beanName, AsyncResult<String> result) {
        log.info("Verticle bean {} of type {} deployment succeeded, id: {}", beanName, typeName, result.result());
    }
}
