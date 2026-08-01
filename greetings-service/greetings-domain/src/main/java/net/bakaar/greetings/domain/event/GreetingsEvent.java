package net.bakaar.greetings.domain.event;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
public class GreetingsEvent {
    protected final LocalDateTime raiseAt = LocalDateTime.now(ZoneId.of("UTC"));
}
