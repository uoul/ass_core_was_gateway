package ass.core;

import ass.core.verticle.WasSocketVerticle;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.vertx.core.Vertx;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class WasGateway {

    @Inject
    Vertx vertx;

    @ConfigProperty(name = "was.ip")
    String wasIp;

    @ConfigProperty(name = "was.port")
    Integer wasPort;

    void onStart(@Observes StartupEvent ev) {

        vertx.deployVerticle(new WasSocketVerticle(wasIp, wasPort));
    }

    void onStop(@Observes ShutdownEvent ev) {

    }
}
