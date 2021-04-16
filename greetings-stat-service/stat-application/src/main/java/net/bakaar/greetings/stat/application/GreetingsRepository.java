package net.bakaar.greetings.stat.application;

import net.bakaar.greetings.stat.application.readmodel.Greeting;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface GreetingsRepository {

    Mono<Greeting> getGreetingForIdentifier(UUID identifier);
}
