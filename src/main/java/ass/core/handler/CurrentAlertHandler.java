package ass.core.handler;

import ass.core.businessobject.AlertMessages;
import io.quarkus.logging.Log;
import io.quarkus.vertx.ConsumeEvent;
import io.vertx.core.Vertx;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.logging.Logger;

@ApplicationScoped
public class CurrentAlertHandler {
    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
    private AlertMessages alertMsgsBackup = AlertMessages.builder().build();
    @Inject
    Vertx vertx;

    @ConsumeEvent(value = "currentAlerts")
    public void calcChanges (String currentAlertsStr) {
        AlertMessages currentAlerts = AlertMessages.fromJson(currentAlertsStr);
        if (!new HashSet<>(currentAlerts.getAlertMessages()).containsAll(alertMsgsBackup.getAlertMessages()) || !new HashSet<>(alertMsgsBackup.getAlertMessages()).containsAll(currentAlerts.getAlertMessages())) {
            Log.info("============= !!! Alerts has changed !!! =============");
            Log.info("Old: " + alertMsgsBackup.toJson());
            Log.info("New: " + currentAlerts.toJson());
            vertx.eventBus().publish("currentAlertsChanged", currentAlertsStr);
            alertMsgsBackup = currentAlerts;
        }
    }
}
