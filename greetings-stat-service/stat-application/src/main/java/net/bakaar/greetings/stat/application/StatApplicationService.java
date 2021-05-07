package net.bakaar.greetings.stat.application;

import lombok.RequiredArgsConstructor;
import net.bakaar.greetings.stat.domain.GreetingCreated;
import net.bakaar.greetings.stat.domain.GreetingsStats;
import net.bakaar.greetings.stat.domain.StatRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StatApplicationService {

    private final StatRepository statRepository;
    private final GreetingsRepository greetingsRepository;

    public Mono<Void> handle(GreetingCreated event) {
        return Mono.zip(
                greetingsRepository.getGreetingForIdentifier(event.identifier()),
                Mono.fromCallable(statRepository::pop),
                (greeting, greetingsStats) -> greetingsStats.increaseCounterFor(greeting.type())
        )
                .flatMap(stats -> {
                    // FIXME Why Mono.fromCallable doesn't work here ? Because of the void return ?
                    statRepository.put(stats);
                    return Mono.empty();
                });
    }

    public Mono<GreetingsStats> retrieveGreetingsStats() {
        return Mono.fromCallable(statRepository::pop);
    }
}