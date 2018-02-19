package pl.jojczykp.kafka_cqrs.notifier.spring_vertx;

import io.vertx.core.AsyncResult;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class VerticleDeploymentProcessor implements BeanPostProcessor {

    @Autowired
    private Vertx vertx;

    @Override
    public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName) {
        if (bean instanceof Verticle) {
            deployVerticle((Verticle) bean, beanName);
        }

        return bean;
    }

    private void deployVerticle(Verticle verticle, String beanName) {
        vertx.deployVerticle(verticle, result ->
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

        System.out.println(msg);

        throw new BeanInitializationException(msg, result.cause());
    }

    private void handleSuccess(String typeName, String beanName, AsyncResult<String> result) {
        System.out.println(String.format("Verticle bean %s of type %s deployment succeeded, id: %s",
                beanName, typeName, result.result()));
    }
}
