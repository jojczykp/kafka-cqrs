package pl.jojczykp.kafka_cqrs.notifier.spring_vertx;

import io.vertx.core.AsyncResult;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BooleanSupplier;

@Component
public class VertexDeployer {

    private static final Duration AWAIT_LIMIT = Duration.ofSeconds(5);
    private static final Duration AWAIT_POLL = Duration.ofMillis(100);

    @Autowired
    private Vertx vertx;

    @EventListener
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        Map<String, Verticle> verticles = event.getApplicationContext().getBeansOfType(Verticle.class);
        ConcurrentHashMap<String, AsyncResult<String>> namesToResults = new ConcurrentHashMap<>();

        startDeployment(verticles, namesToResults);
        awaitUntil(AWAIT_LIMIT, () -> namesToResults.size() == verticles.size());
        verifyResults(verticles, namesToResults);
    }

    private void startDeployment(Map<String, Verticle> verticles, ConcurrentHashMap<String, AsyncResult<String>> namesToResults) {
        for (Map.Entry<String, Verticle> entry : verticles.entrySet()) {
            String beanName = entry.getKey();
            Verticle verticle = entry.getValue();

            vertx.deployVerticle(verticle, result -> namesToResults.put(beanName, result));
        }
    }

    private void verifyResults(Map<String, Verticle> verticles, ConcurrentHashMap<String, AsyncResult<String>> namesToResults) {
        for (Map.Entry<String, AsyncResult<String>> entry : namesToResults.entrySet()) {
            String beanName = entry.getKey();
            AsyncResult<String> result = entry.getValue();
            String typeName = verticles.get(beanName).getClass().getName();

            if (result.failed()) {
                handleFailure(typeName, beanName, result);
            } else {
                handleSuccess(typeName, beanName, result);
            }
        }

        verifyAllDeployed(verticles, namesToResults);
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

    private void verifyAllDeployed(Map<String, Verticle> verticles, ConcurrentHashMap<String, AsyncResult<String>> namesToResults) {
        if (namesToResults.size() != verticles.size()) {
            String msg = "Couldn't start all verticles in " + AWAIT_LIMIT;
            System.out.println(msg);
            throw new BeanInitializationException(msg);
        }
    }

    private void awaitUntil(Duration remaining, BooleanSupplier condition) {
        try {
            while (!condition.getAsBoolean()) {
                Thread.sleep(AWAIT_POLL.toMillis());
                remaining = remaining.minus(AWAIT_POLL);

                if (condition.getAsBoolean() || remaining.isNegative()) {
                    break;
                }
            }
        } catch (InterruptedException e) {
            /* Ignore */
        }
    }
}
