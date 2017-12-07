package de.codepitbull.vertx.kubernetes.frontend.simple;

import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.StaticHandler;
import io.vertx.reactivex.ext.web.handler.sockjs.SockJSHandler;

public class SimpleVerticle extends AbstractVerticle {

    public static final int PORT = 8666;

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        router.route("/simple/static/*").handler(StaticHandler.create("webroot-simple"));
        router.route("/simple/eventbus/*").handler(sockJsHandler());
        router.get("/simple/world").handler(r -> r.response().end("Hello World !!!"));

        vertx.createHttpServer().requestHandler(router::accept).listen(PORT);

        vertx.periodicStream(1000)
                .handler(a -> vertx.eventBus().send("example-browser", "from "+ this.getClass().getSimpleName()));

        vertx.eventBus().consumer("example-server")
                .handler(a -> System.out.println((a.body())));

        System.out.println(this.getClass().getSimpleName() + "on port " + PORT);
    }

    public SockJSHandler sockJsHandler() {
        SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
        BridgeOptions options = new BridgeOptions()
            .addOutboundPermitted(new PermittedOptions().setAddress("example-browser"))
            .addInboundPermitted(new PermittedOptions().setAddress("example-server"));
        sockJSHandler.bridge(options);
        return sockJSHandler;
    }
}
