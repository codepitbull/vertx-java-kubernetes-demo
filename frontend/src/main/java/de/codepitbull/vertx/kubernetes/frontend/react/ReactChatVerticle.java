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
        router.route("/react/static/*").handler(StaticHandler.create("webroot-react"));
        router.route("/react/eventbus/*").handler(sockJsHandler());

        MessageProducer sender = vertx.eventBus().publisher("browser");

        vertx.eventBus()
            .<String>consumer("server")
            .toFlowable()
            .map(msg -> vertx.eventBus().<String>rxSend("profanitycheck", msg.body()))
            .subscribe(res ->
                res.subscribe(
                    answer -> sender.write(new JsonObject()
                        .put("msg", answer.body())
                        .put("date", Calendar.getInstance().getTime().toString())),
                    failure -> sender.write(new JsonObject()
                        .put("msg", "----BIG BROTHER ISN'T WATCHING----")
                        .put("date", Calendar.getInstance().getTime().toString()))
                )
            );

        vertx.createHttpServer().requestHandler(router::accept).listen(PORT);

    }

    public SockJSHandler sockJsHandler() {
        SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
        BridgeOptions options = new BridgeOptions()
            .addOutboundPermitted(new PermittedOptions().setAddress("browser"))
            .addInboundPermitted(new PermittedOptions().setAddress("server"));
        sockJSHandler.bridge(options);
        return sockJSHandler;
    }

}
