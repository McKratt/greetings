package net.bakaar.greetings.domain;

@FunctionalInterface
public interface MessageCreator {

    String createMessage(String name);
}
