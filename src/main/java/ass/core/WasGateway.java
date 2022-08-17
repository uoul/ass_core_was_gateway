package ass.core;

import ass.core.verticle.WasSocketVerticle;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.vertx.core.Vertx;
import org.eclipse.microprofile.config.ConfigProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class WasGateway {

    @Inject
    Vertx vertx;

    void onStart(@Observes StartupEvent ev) {
        vertx.deployVerticle(new WasSocketVerticle(ConfigProvider.getConfig().getValue("was.ip", String.class), ConfigProvider.getConfig().getValue("was.port", Integer.class)));
    }

    void onStop(@Observes ShutdownEvent ev) {

    }
}
