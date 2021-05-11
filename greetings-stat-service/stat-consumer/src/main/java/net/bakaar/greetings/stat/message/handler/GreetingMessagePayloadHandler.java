package net.bakaar.greetings.stat.message.handler;

import reactor.core.publisher.Mono;

import java.net.URI;

public interface GreetingMessagePayloadHandler {

    boolean canHandle(URI type);

    Mono<Void> handle(String payload);
}
