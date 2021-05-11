package net.bakaar.greetings.stat.rest.client;

import lombok.RequiredArgsConstructor;
import net.bakaar.greetings.stat.application.GreetingsRepository;
import net.bakaar.greetings.stat.application.readmodel.Greeting;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class GreetingsRepositoryAdapter implements GreetingsRepository {

    private final WebClient client;
    private final GreetingsStatRestClientProperties properties;

    @Override
    public Mono<Greeting> getGreetingForIdentifier(UUID identifier) {
        return client.
                get()
                .uri(properties.getUrl() + "/" + identifier.toString())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Greeting.class);
    }
}
