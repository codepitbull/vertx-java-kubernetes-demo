package de.codepitbull.vertx.kubernetes.mqtt;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttServer;
import io.vertx.mqtt.MqttTopicSubscription;

import java.util.ArrayList;
import java.util.List;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        MqttServer server = MqttServer.create(vertx);

        server.endpointHandler(endpoint -> {
            endpoint.subscribeHandler(s -> {
                List<MqttQoS> grantedQosLevels = new ArrayList<>();
                for(MqttTopicSubscription sub:s.topicSubscriptions()) {
                    vertx.eventBus()
                        .<Buffer>consumer("mqtt-"+sub.topicName())
                        .handler(msg -> endpoint.publish(sub.topicName(), msg.body(), MqttQoS.AT_MOST_ONCE, false, false));
                    grantedQosLevels.add(sub.qualityOfService());
                }
                endpoint.subscribeAcknowledge(s.messageId(), grantedQosLevels);
            });

            endpoint.publishHandler(s -> vertx.eventBus().publish("mqtt-"+s.topicName(), s.payload()));

            // accept connection from the remote client
            endpoint.accept(false);
        });

        server.listen(1883);
    }

}
