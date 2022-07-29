package ass.core;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class WasGateway {

    void onStart(@Observes StartupEvent ev) {

    }

    void onStop(@Observes ShutdownEvent ev) {

    }
}
