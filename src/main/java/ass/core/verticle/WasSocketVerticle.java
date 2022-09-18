package ass.core.verticle;

import ass.core.WasXmlInterpreter;
import io.quarkus.logging.Log;
import io.quarkus.runtime.Quarkus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;

import java.util.concurrent.atomic.AtomicReference;

public class WasSocketVerticle extends AbstractVerticle {
    private final String wasHost;
    private final int wasPort;
    private final AtomicReference<String> msg = new AtomicReference<>("");
    private NetClient tcpClient;

    public WasSocketVerticle(String wasHost, Integer wasPort) {
        this.wasHost = wasHost;
        this.wasPort = wasPort;
    }

    void startWasSocket() {
        tcpClient.connect(wasPort, wasHost, res -> {
            if (res.succeeded()) {
                NetSocket socket = res.result();
                Log.info(String.format("Connected to WAS_HOST: %s WAS_PORT: %d", wasHost, wasPort));
                socket.write("GET_MESSAGE\n");
                socket.handler(buffer -> {
                    msg.set(msg.get() + buffer.getString(0, buffer.length()));
                    if (msg.get().contains("</pdu>")) {
                        vertx.eventBus().publish("currentAlerts", WasXmlInterpreter.parseXmlToAlertMgs(msg.get()).toJson());
                        msg.set("");
                        vertx.setTimer(11000, timerId -> {
                            socket.write("GET_MESSAGE\n");
                        });
                    }
                });
                socket.closeHandler((closeResult) -> {
                    Log.error(String.format("Connection to WAS has been closed! - WAS_HOST: %s WAS_PORT: %d", wasHost, wasPort));
                    startWasSocket();
                });
            } else {
                Log.error(String.format("Cannot connect to WAS - WAS_HOST: %s WAS_PORT: %d", wasHost, wasPort));
                Quarkus.asyncExit(1);
            }
        });
    }

    @Override
    public void start() {
        NetClientOptions options = new NetClientOptions()
                .setReconnectAttempts(10);
        tcpClient = vertx.createNetClient(options);
        startWasSocket();
    }

    @Override
    public void stop() {

    }
}
