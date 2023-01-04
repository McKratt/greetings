package net.bakaar.greetings.stat.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bakaar.greetings.stat.domain.GreetingCreated;
import net.bakaar.greetings.stat.domain.GreetingsStats;
import net.bakaar.greetings.stat.domain.StatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatApplicationService {

    private final StatRepository statRepository;
    private final GreetingsRepository greetingsRepository;

    @Transactional
    public Mono<Void> handle(GreetingCreated event) {
        return Mono.zip(
                greetingsRepository.getGreetingForIdentifier(event.identifier()),
                Mono.fromFuture(statRepository.pop()),
                (greeting, greetingsStats) -> greetingsStats.increaseCounterFor(greeting.type())
        ).flatMap(stats -> Mono.fromRunnable(() -> statRepository.put(stats)));
    }

    public Mono<GreetingsStats> retrieveGreetingsStats() {
        return Mono.fromFuture(statRepository::pop);
    }
}