package de.codepitbull.vertx.kubernetes.frontend.react;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.MessageProducer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.StaticHandler;
import io.vertx.reactivex.ext.web.handler.sockjs.SockJSHandler;

import java.util.Calendar;

public class ReactChatVerticle extends AbstractVerticle {

    public static final int PORT = 8667;

    @Override
    public void start() throws Exception {


        Router router = Router.router(vertx);
        router.get("/react/alive").handler(r -> r.response().end("I AM ALIVE"));

        vertx.createHttpServer().requestHandler(router::accept).listen(PORT);

    }

}
