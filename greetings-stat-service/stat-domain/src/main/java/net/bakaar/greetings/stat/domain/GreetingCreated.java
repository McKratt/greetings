package net.bakaar.greetings.stat.domain;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

public record GreetingCreated(UUID identifier, LocalDateTime raisedAt) implements GreetingEvent {

    public final static URI TYPE = URI.create("http://bakaar.net/greetings/events/greeting-created");

    @Override
    public URI type() {
        return TYPE;
    }
}
