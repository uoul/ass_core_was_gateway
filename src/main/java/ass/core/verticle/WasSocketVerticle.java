package ass.core.verticle;

import ass.core.WasXmlInterpreter;
import ass.core.businessobject.AlertMsg;
import io.quarkus.logging.Log;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;

import java.util.HashSet;
import java.util.Vector;
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
        NetClientOptions options = new NetClientOptions()
                .setReconnectAttempts(5);
        NetClient tcpClient = vertx.createNetClient(options);
        tcpClient.connect(wasPort, wasHost, res -> {
            if (res.succeeded()) {
                NetSocket socket = res.result();
                Log.info("Connected to WAS-Host: " + wasHost + " Port: " + wasPort);
                socket.write("GET_MESSAGE\n");
                socket.handler(buffer -> {
                    msg.set(msg.get() + buffer.getString(0, buffer.length()));
                    if (msg.get().contains("</pdu>")) {
                        vertx.eventBus().publish("currentAlerts", WasXmlInterpreter.parseToJson(WasXmlInterpreter.parseXmlToAlertMgs(msg.get())));
                        msg.set("");
                        vertx.setTimer(11000, timerId -> {
                            socket.write("GET_MESSAGE\n");
                        });
                    }
                });
            } else {
                Log.error("Cannot connect to Host: " + wasHost + " Port: " + wasPort);
            }
        });
    }

    @Override
    public void stop() {

    }
}
