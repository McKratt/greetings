package net.bakaar.greetings.stat.application.readmodel;

import net.bakaar.greetings.stat.domain.GreetingType;

public record Greeting(GreetingType type, String name) {
}
