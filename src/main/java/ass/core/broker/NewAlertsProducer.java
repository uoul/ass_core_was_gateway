package ass.core.broker;

import io.quarkus.vertx.ConsumeEvent;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class NewAlertsProducer {

    @Inject
    @Channel("new_alerts_out")
    Emitter<String> emitter;

    @ConsumeEvent("newAlerts")
    void consumeAlerts(String newAlerts) {
        emitter.send(newAlerts);
    }
}
