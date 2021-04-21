package net.bakaar.greetings.stat.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CounterRepository extends ReactiveCrudRepository<Counter, Long> {
    Mono<Counter> findCounterByNameEquals(String name);
}
