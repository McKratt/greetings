package net.bakaar.greetings.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class GreetingCreatedEvent extends GreetingsEvent {
    private final UUID identifier;
}
