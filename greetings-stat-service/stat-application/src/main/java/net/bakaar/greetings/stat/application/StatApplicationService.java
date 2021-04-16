package net.bakaar.greetings.stat.application;

import lombok.extern.slf4j.Slf4j;
import net.bakaar.greetings.stat.domain.GreetingCreated;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class StatApplicationService {
    public Mono<Void> handle(GreetingCreated event) {
        log.info("Event : " + event.toString());
        return Mono.empty();
    }
}
