package net.bakaar.greetings.stat.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bakaar.greetings.stat.domain.GreetingCreated;
import net.bakaar.greetings.stat.domain.GreetingsStats;
import net.bakaar.greetings.stat.domain.StatRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatApplicationService {

    private final StatRepository statRepository;
    private final GreetingsRepository greetingsRepository;

    // TODO make it transactional
    public Mono<Void> handle(GreetingCreated event) {
        return Mono.zip(
                greetingsRepository.getGreetingForIdentifier(event.identifier()),
                Mono.just(statRepository.pop()),
                (greeting, greetingsStats) -> greetingsStats.increaseCounterFor(greeting.type())
        )
                .log(log.getName())
                .flatMap(stats -> {
                    // FIXME Why Mono.fromCallable doesn't work here ? Because of the void return ?
                    log.debug("stats counters size = " + stats.getCounters().size());
                    statRepository.put(stats);
                    return Mono.empty();
                });
    }

    public Mono<GreetingsStats> retrieveGreetingsStats() {
        log.debug("IN THE SERVICE - retrieveGreetingsStats");
        return Mono.fromCallable(statRepository::pop);
    }
}