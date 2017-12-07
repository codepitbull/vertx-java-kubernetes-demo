package de.codepitbull.vertx.kubernetes.mqtt;

import io.vertx.core.Vertx;

public class MainVerticleMain {
    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(MainVerticle.class.getName());
    }
}
