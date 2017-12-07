package de.codepitbull.vertx.kubernetes.frontend.kubernetes;

import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.config.ConfigRetriever;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.kubernetes.KubernetesServiceImporter;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public class KubernetesVerticle extends AbstractVerticle {

    public static final String SERVICENAME = "servicename";
    public static final int PORT = 8668;

    ServiceDiscovery discovery;
    Optional<JsonObject> config = empty();

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        router.get("/kubernetes/services/:" + SERVICENAME).handler(this::getServiceRecord);
        router.get("/kubernetes/configmap").handler(this::getConfigMap);

        discovery = ServiceDiscovery.create(vertx.getDelegate());
        discovery.registerServiceImporter(new KubernetesServiceImporter(), new JsonObject());

        initConfigMap();
        vertx.createHttpServer().requestHandler(router::accept).listen(PORT);

        System.out.println(this.getClass().getSimpleName() + "on port " + PORT);
    }

    public void getServiceRecord(RoutingContext rc) {
        discovery
                .getRecord(new JsonObject().put("name", rc.request().getParam(SERVICENAME)),
                        res -> rc.response().end(Json.encodePrettily(res.result())));

    }

    public void getConfigMap(RoutingContext rc) {
        rc.response().end(config.orElseGet(() -> new JsonObject()).encodePrettily());
    }


    public void initConfigMap() {
        ConfigStoreOptions store = new ConfigStoreOptions()
                .setType("configmap")
                .setConfig(new JsonObject()
                        .put("namespace", "default")
                        .put("name", "vertx-config")
                );

        ConfigRetriever retriever = ConfigRetriever.create(vertx,
                new ConfigRetrieverOptions().addStore(store));

        retriever.configStream().toFlowable()
                .subscribe(cfgMap -> config = of(cfgMap));
    }
}
