package ass.core.handler;

import ass.core.businessobject.AlertMessages;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import io.quarkus.vertx.ConsumeEvent;
import io.vertx.core.Vertx;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;

@ApplicationScoped
public class CurrentAlertHandler {
    private AlertMessages alertMsgsBackup;
    @Inject
    Vertx vertx;

    public CurrentAlertHandler() {
        try {
            String alertsStateStr = Files.readString(Paths.get(ConfigProvider.getConfig().getValue("state.path", String.class) + "/currentAlerts.json"));
            ObjectMapper mapper = new ObjectMapper();
            alertMsgsBackup = mapper.readValue(alertsStateStr, AlertMessages.class);
        } catch (IOException e) {
            alertMsgsBackup = AlertMessages.builder().build();
        }
    }

    @ConfigProperty(name = "state.path")
    String statePath;

    private void writeAlertsToFile(AlertMessages alertMessages, String filePath){
        try {
            Writer fileWriter = new FileWriter(filePath, false); //overwrites file
            fileWriter.write(alertMessages.toJson());
            fileWriter.close();
        } catch (IOException e) {
            Log.error("Unable to write statefile! - " + e.getMessage());
        }
    }

    @ConsumeEvent(value = "currentAlerts")
    public void calcChanges (String currentAlertsStr) {
        AlertMessages currentAlerts = AlertMessages.fromJson(currentAlertsStr);

        Log.info("Backup: " + alertMsgsBackup.toJson());
        Log.info("current: " + currentAlerts.toJson());

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
            writeAlertsToFile(currentAlerts, statePath + "/currentAlerts.json");
            alertMsgsBackup = currentAlerts;
        }
    }
}
