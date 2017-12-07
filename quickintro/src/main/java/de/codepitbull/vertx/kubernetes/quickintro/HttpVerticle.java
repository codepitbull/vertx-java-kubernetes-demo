package de.codepitbull.vertx.kubernetes.quickintro;

import io.vertx.reactivex.core.AbstractVerticle;

public class HttpVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        vertx.createHttpServer().requestHandler(req -> req.response().end("Hello World")).listen(8080);
    }

}
