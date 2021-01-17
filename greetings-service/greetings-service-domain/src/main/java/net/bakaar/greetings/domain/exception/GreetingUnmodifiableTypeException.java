package net.bakaar.greetings.domain.exception;

import static java.lang.String.format;

public class GreetingUnmodifiableTypeException extends IllegalArgumentException {

    public static final String ERROR_MESSAGE_PATTERN = "The Type %s is not modifiable to %s !";

    public GreetingUnmodifiableTypeException(String typeFrom, String typeTo) {
        super(format(ERROR_MESSAGE_PATTERN, typeFrom, typeTo));
    }

    public GreetingUnmodifiableTypeException(String typeFrom, String typeTo, Throwable cause) {
        super(format(ERROR_MESSAGE_PATTERN, typeFrom, typeTo), cause);
    }

    public GreetingUnmodifiableTypeException(Throwable cause) {
        throw new UnsupportedOperationException();
    }
}
