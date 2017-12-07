package de.codepitbull.vertx.kubernetes.backend;

import io.vertx.reactivex.core.AbstractVerticle;

import java.util.List;
import java.util.stream.Collectors;

import static de.codepitbull.vertx.kubernetes.backend.Tuple.tuple;

public class ProfanityFilterVerticle extends AbstractVerticle {

    private List<String> blockList = List.of("blume", "schmetterling", "sonnenblume", "fasching", "afd");

    @Override
    public void start() throws Exception {

        vertx.eventBus()
                .<String>consumer("profanitycheck")
                .toFlowable()
                .map(msg -> tuple(msg, List.of(msg.body().split(" "))))
                .map(msgAndStrings -> {
                    List<String> filtered = msgAndStrings.y.stream()
                            .map(wordFromUser -> {
                                if(blockList.contains(wordFromUser.toLowerCase()))
                                    return "---BIG BROTHER DOESN'T APPROVE---";
                                else
                                    return wordFromUser;
                            }).collect(Collectors.toList());
                    return tuple(msgAndStrings.x, String.join(" ", filtered));
                })
                .subscribe(res -> res.x.reply(res.y));
    }

}
