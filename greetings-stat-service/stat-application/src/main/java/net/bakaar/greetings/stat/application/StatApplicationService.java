package net.bakaar.greetings.stat.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bakaar.greetings.stat.domain.GreetingCreated;
import net.bakaar.greetings.stat.domain.StatRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatApplicationService {

    private final StatRepository statRepository;
    private final GreetingsRepository greetingsRepository;

    public Mono<Void> handle(GreetingCreated event) {
        log.debug("Event : " + event.toString());
        return Mono.zip(
                greetingsRepository.getGreetingForIdentifier(event.identifier()),
                Mono.fromFuture(statRepository.pop()),
                (greeting, greetingsStats) -> greetingsStats.increaseCounterFor(greeting.type())
        )
                .flatMap(stats -> Mono.fromFuture(statRepository.put(stats)));
    }
}