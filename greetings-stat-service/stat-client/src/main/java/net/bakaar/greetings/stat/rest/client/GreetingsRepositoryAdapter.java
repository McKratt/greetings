package net.bakaar.greetings.stat.rest.client;

import lombok.RequiredArgsConstructor;
import net.bakaar.greetings.stat.application.GreetingsRepository;
import net.bakaar.greetings.stat.application.readmodel.Greeting;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class GreetingsRepositoryAdapter implements GreetingsRepository {

    private final GreetingsRestClient client;

    @Override
    public Mono<Greeting> getGreetingForIdentifier(UUID identifier) {
        return client.findGreetingFromId(identifier);
    }
}
