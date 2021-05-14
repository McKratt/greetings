package net.bakaar.greetings.domain.event;

public interface EventEmitter {
    void emit(GreetingsEvent event);
}
