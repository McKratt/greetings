package net.bakaar.greetings.stat.message.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonDeserializationException extends RuntimeException {
    public JsonDeserializationException(JsonProcessingException e) {
        super(e);
    }
}
