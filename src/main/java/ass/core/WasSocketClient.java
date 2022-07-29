package ass.core;

import ass.core.BusinessObjects.AlertMsg;
import ass.core.Data.ActiveAlertMsgs;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.inject.Inject;
import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@ClientEndpoint
public class WasSocketClient {

    @Inject
    ActiveAlertMsgs activeAlertMsgs;

    @Channel("new_alert_out")
    Emitter<String> newAlertEmitter;

    @Channel("active_alerts_out")
    Emitter<String> activeAlertsEmitter;

    ObjectMapper mapper = new ObjectMapper();

    @OnOpen
    public void open(Session session) {
        activeAlertMsgs.clearAlertMsgs();
    }

    @OnMessage
    void message(String msg) {

    }

    void sendNewAlertToBroker(AlertMsg msg) {
        try {
            String msgStr = mapper.writeValueAsString(msg);
            newAlertEmitter.send(msgStr).whenComplete((res, err) -> {
                if (err == null)
                    Log.info("Successfully sent new alert to Broker -> " + msgStr);
                else
                    Log.error("Failed to send new alert to Broker -> " + msgStr);
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    void sendActiveAlertsToBroker(ActiveAlertMsgs msgs) {
        try {
            String msgStr = mapper.writeValueAsString(msgs);
            activeAlertsEmitter.send(msgStr).whenComplete((res, err) -> {
                if (err == null)
                    Log.info("Successfully sent new alert to Broker -> " + msgStr);
                else
                    Log.error("Failed to send new alert to Broker -> " + msgStr);
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
