package pl.jojczykp.kafka_cqrs.test_utils.tcp;

import java.io.IOException;
import java.net.ServerSocket;

public class TcpUtils {

    private TcpUtils() {}

    public static int getFreePort() {
        try {
            return new ServerSocket(0).getLocalPort();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
