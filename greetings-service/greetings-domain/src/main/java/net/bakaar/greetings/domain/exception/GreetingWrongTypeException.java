package net.bakaar.greetings.domain.exception;

import static java.lang.String.format;

public class GreetingWrongTypeException extends IllegalArgumentException {

    public static final String ERROR_MESSAGE_PATTERN = "The Type %s is unknown !";

    public GreetingWrongTypeException(String type, Throwable cause) {
        super(format(ERROR_MESSAGE_PATTERN, type), cause);
    }
}
