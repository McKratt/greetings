package net.bakaar.greetings.stat.message.exception;

public class JsonDeserializationException extends RuntimeException {
    public JsonDeserializationException(Throwable e) {
        super(e);
    }
}
