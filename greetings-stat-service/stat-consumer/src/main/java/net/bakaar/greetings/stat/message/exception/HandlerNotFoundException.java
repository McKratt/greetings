package net.bakaar.greetings.stat.message.exception;

import java.net.URI;

public class HandlerNotFoundException extends RuntimeException {
    public HandlerNotFoundException(URI type) {
        super("No handler found for message with payload type : " + type.toString());
    }
}
