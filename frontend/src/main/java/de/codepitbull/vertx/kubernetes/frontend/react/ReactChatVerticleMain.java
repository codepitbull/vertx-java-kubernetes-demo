package de.codepitbull.vertx.kubernetes.frontend.react;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class ReactChatVerticleMain {
    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(ReactChatVerticle.class, new DeploymentOptions());
    }
}
