package net.bakaar.greetings.domain;

import java.util.Optional;
import java.util.UUID;

public interface GreetingRepository {
    Greeting put(Greeting greeting);

    Optional<Greeting> find(UUID identifier);
}
