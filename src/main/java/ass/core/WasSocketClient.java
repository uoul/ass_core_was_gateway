package ass.core;

import ass.core.BusinessObjects.AlertMsg;
import ass.core.Data.ActiveAlertMsgs;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Blocking;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.inject.Inject;
import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import java.util.Vector;

@ClientEndpoint
public class WasSocketClient {

    @ConfigProperty(name = "was.request.sleep")
    long requestSleepTime;

    @Inject
    ActiveAlertMsgs activeAlertMsgs;

    @Channel("new_alert_out")
    Emitter<String> newAlertEmitter;

    @Channel("active_alerts_out")
    Emitter<String> activeAlertsEmitter;

    ObjectMapper mapper = new ObjectMapper();
    String currentMsg;
    Session session;

    @OnOpen
    public void open(Session session) {
        activeAlertMsgs.clearAlertMsgs();
        currentMsg = "";
        session = this.session;
        this.session.getAsyncRemote().sendText("GET_MESSAGES");
    }

    @OnMessage@Blocking
    void message(String msg) {
        currentMsg = currentMsg + msg;
        if (currentMsg.endsWith("</pdu>")) {
            Vector<AlertMsg> newAlerts = new Vector<>();
            Vector<AlertMsg> backupAlertMsgs = new Vector<>(activeAlertMsgs.getActiveMsgs());
            activeAlertMsgs.setActiveMsgs(WasXmlInterpreter.parseXmlToAlertMgs(currentMsg));
            activeAlertMsgs.getActiveMsgs().forEach((activeMsg) -> {
                if (!backupAlertMsgs.contains(activeMsg))
                    newAlerts.add(activeMsg);
            });
            sendActiveAlertsToBroker(activeAlertMsgs.getActiveMsgs());
            sendNewAlertToBroker(newAlerts);
            currentMsg = "";
            // wait for next request
            try {
                Thread.sleep(requestSleepTime);
                this.session.getAsyncRemote().sendText("GET_MESSAGES");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    void sendNewAlertToBroker(Vector<AlertMsg> msgs) {
        try {
            String msgStr = mapper.writeValueAsString(msgs);
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

    void sendActiveAlertsToBroker(Vector<AlertMsg> msgs) {
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
