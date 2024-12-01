package net.bakaar.greetings.domain.exception;

public class GreetingUnmodifiableTypeException extends IllegalArgumentException {

    public static final String ERROR_MESSAGE_PATTERN = "The Type %s is not modifiable to %s !";

    public GreetingUnmodifiableTypeException(String typeFrom, String typeTo) {
        super(ERROR_MESSAGE_PATTERN.formatted(typeFrom, typeTo));
    }
}
