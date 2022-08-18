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
    private AlertMessages alertMsgsBackup = AlertMessages.builder().build();
    @Inject
    Vertx vertx;

    @ConsumeEvent(value = "currentAlerts")
    public void calcChanges (String currentAlertsStr) {
        AlertMessages currentAlerts = AlertMessages.fromJson(currentAlertsStr);

        // Send new alerts to RabbitMQ
        AlertMessages newAlerts = AlertMessages.builder().build();
        currentAlerts.getAlertMessages().forEach(msg -> {
            if (!alertMsgsBackup.getAlertMessages().contains(msg)){
                newAlerts.getAlertMessages().add(msg);
            }
        });
        if (newAlerts.getAlertMessages().size() > 0) {
            vertx.eventBus().publish("newAlerts", newAlerts.toJson());
        }

        // Send active alerts to RabbitMQ
        if (!new HashSet<>(currentAlerts.getAlertMessages()).containsAll(alertMsgsBackup.getAlertMessages()) || !new HashSet<>(alertMsgsBackup.getAlertMessages()).containsAll(currentAlerts.getAlertMessages())) {
            Log.info("============= !!! Alerts has changed !!! =============");
            Log.info("Old: " + alertMsgsBackup.toJson());
            Log.info("New: " + currentAlerts.toJson());
            vertx.eventBus().publish("activeAlerts", currentAlerts.toJson());
            alertMsgsBackup = currentAlerts;
        }
    }
}
