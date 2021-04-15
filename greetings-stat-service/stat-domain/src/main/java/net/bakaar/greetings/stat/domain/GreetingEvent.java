package net.bakaar.greetings.stat.domain;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

public interface GreetingEvent {
    UUID identifier();

    URI type();

    LocalDateTime raisedAt();
}
