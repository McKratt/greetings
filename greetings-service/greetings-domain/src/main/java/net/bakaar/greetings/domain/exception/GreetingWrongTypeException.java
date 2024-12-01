package net.bakaar.greetings.domain.exception;

public class GreetingWrongTypeException extends IllegalArgumentException {

    public static final String ERROR_MESSAGE_PATTERN = "The Type %s is unknown !";

    public GreetingWrongTypeException(String type, Throwable cause) {
        super(ERROR_MESSAGE_PATTERN.formatted(type), cause);
    }
}
