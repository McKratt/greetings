package net.bakaar.greetings.stat.rest.glue;

import net.bakaar.greetings.stat.application.GreetingsRepository;
import net.bakaar.greetings.stat.application.readmodel.Greeting;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class TestGreetingsRepository implements GreetingsRepository {
    private final Map<UUID, Greeting> greetings = new HashMap<>();

    void setGreeting(Greeting greeting, UUID identifier) {
        this.greetings.put(identifier, greeting);
    }

    @Override
    public Mono<Greeting> getGreetingForIdentifier(UUID identifier) {
        return Mono.just(greetings.get(identifier));
    }
}
