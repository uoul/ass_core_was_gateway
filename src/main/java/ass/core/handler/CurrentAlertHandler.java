package ass.core.handler;

import ass.core.businessobject.AlertMsg;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import io.quarkus.vertx.ConsumeEvent;
import io.vertx.core.Vertx;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.Vector;

@ApplicationScoped
public class CurrentAlertHandler {
    private Vector<AlertMsg> alertMsgsBackup = new Vector<>();
    @Inject
    Vertx vertx;

    @ConsumeEvent(value = "currentAlerts")
    public void calcChanges (String currentAlertsStr) {
        ObjectMapper mapper = new ObjectMapper();
        Vector<AlertMsg> currentAlerts = new Vector<>();
        try {
            currentAlerts = mapper.readValue(currentAlertsStr, currentAlerts.getClass());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (!new HashSet<>(currentAlerts).containsAll(alertMsgsBackup) || !new HashSet<>(alertMsgsBackup).containsAll(currentAlerts)) {
            Log.info("!!! Alerts has changed !!!");
            Log.info("Old: " + alertMsgsBackup.toString());
            Log.info("New: " + currentAlertsStr);
            vertx.eventBus().publish("currentAlertsChanged", currentAlertsStr);
            alertMsgsBackup = currentAlerts;
        }
    }
}
