package net.bakaar.greetings.domain.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.bakaar.greetings.domain.Greeting;

import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GreetingCreated extends GreetingsEvent {
    private final UUID identifier;

    public static GreetingCreated of(Greeting greeting) {
        return new GreetingCreated(greeting.getIdentifier());
    }
}
