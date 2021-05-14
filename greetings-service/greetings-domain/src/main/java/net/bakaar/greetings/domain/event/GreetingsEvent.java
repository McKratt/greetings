package net.bakaar.greetings.domain.event;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GreetingsEvent {
    protected final LocalDateTime raiseAt = LocalDateTime.now();
}
