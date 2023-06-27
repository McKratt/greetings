package net.bakaar.greetings.domain;

import java.util.Objects;

public record CreateGreetingCommand(String type, String name) {
    public CreateGreetingCommand {
        Objects.requireNonNull(type, "Type should not be null !");
        Objects.requireNonNull(name, "Name should not be null !");
    }
}
