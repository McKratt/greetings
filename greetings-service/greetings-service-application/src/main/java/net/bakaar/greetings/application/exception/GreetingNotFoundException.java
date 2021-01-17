package net.bakaar.greetings.application.exception;

public class GreetingNotFoundException extends RuntimeException {
    public GreetingNotFoundException(String message) {
        super(message);
    }
}
