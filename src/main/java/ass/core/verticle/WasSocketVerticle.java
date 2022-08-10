package ass.core.verticle;

import ass.core.WasXmlInterpreter;
import io.quarkus.logging.Log;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.util.concurrent.atomic.AtomicReference;

public class WasSocketVerticle extends AbstractVerticle {
    private final String wasHost;
    private final int wasPort;

    public WasSocketVerticle(String wasHost, Integer wasPort) {
        this.wasHost = wasHost;
        this.wasPort = wasPort;
    }

    @Override
    public void start() {
        AtomicReference<String> msg = new AtomicReference<>("");
        NetClient tcpClient = vertx.createNetClient();
        tcpClient.connect(wasPort, wasHost, res -> {
            NetSocket socket = res.result();
            Log.info("Connected to Host: " + wasHost + " Port: " + wasPort);
            socket.write("GET_MESSAGE\n");
            socket.handler(buffer -> {
                msg.set(msg.get() + buffer.getString(0, buffer.length()));
                if (msg.get().contains("</pdu>")) {
                    vertx.eventBus().publish("alerts", WasXmlInterpreter.parseToJson(WasXmlInterpreter.parseXmlToAlertMgs(msg.get())));
                    msg.set("");
                    vertx.setTimer(12000, timerId -> {
                        socket.write("GET_MESSAGE\n");
                    });
                }
            });
        });
    }

    @Override
    public void stop() {

    }
}
