package pl.jojczykp.kafka_cqrs.test_utils.tcp;

import java.io.IOException;
import java.net.ServerSocket;

public class TcpUtils {

    private TcpUtils() {}

    public static int getFreePort() {
        try {
            ServerSocket ss = new ServerSocket(0);
            ss.close();
            return ss.getLocalPort();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
