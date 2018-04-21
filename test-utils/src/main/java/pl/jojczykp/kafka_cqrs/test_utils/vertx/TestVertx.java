package pl.jojczykp.kafka_cqrs.test_utils.vertx;

import io.vertx.core.Verticle;
import io.vertx.core.Vertx;

import java.util.concurrent.CountDownLatch;

public interface TestVertx extends Vertx {

    static Vertx with(Verticle... verticles) throws InterruptedException {
        Vertx vertx = Vertx.vertx();

        CountDownLatch deploymentDone = new CountDownLatch(verticles.length);

        for (Verticle v : verticles) {
            vertx.deployVerticle(v, e -> deploymentDone.countDown());
        }

        deploymentDone.await();

        return vertx;
    }
}
