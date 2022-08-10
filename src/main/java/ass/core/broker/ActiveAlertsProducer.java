package ass.core.broker;

import io.quarkus.vertx.ConsumeEvent;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ActiveAlertsProducer {
    @Inject
    @Channel("active_alerts_out")
    Emitter<String> emitter;

    @ConsumeEvent(value = "alerts")
    void consumeAlerts(String alerts) {
        emitter.send(alerts);
    }
}
