package net.bakaar.greetings.stat.message.handler;

import java.net.URI;

public interface GreetingMessagePayloadHandler {

    boolean canHandle(URI type);

    void handle(String payload);
}
