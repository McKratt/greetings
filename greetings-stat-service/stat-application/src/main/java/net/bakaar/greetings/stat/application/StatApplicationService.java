package net.bakaar.greetings.stat.application;

import net.bakaar.greetings.stat.domain.GreetingCreated;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class StatApplicationService {
    public Mono<Void> handle(GreetingCreated event) {
        return Mono.empty();
    }
}
