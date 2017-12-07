package de.codepitbull.vertx.kubernetes.frontend;

import de.codepitbull.vertx.kubernetes.frontend.kubernetes.KubernetesVerticle;
import de.codepitbull.vertx.kubernetes.frontend.react.ReactChatVerticle;
import de.codepitbull.vertx.kubernetes.frontend.simple.SimpleVerticle;
import io.vertx.reactivex.core.AbstractVerticle;

public class MainVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        vertx.deployVerticle(SimpleVerticle.class.getName());
        vertx.deployVerticle(ReactChatVerticle.class.getName());
        vertx.deployVerticle(KubernetesVerticle.class.getName());
    }
}
