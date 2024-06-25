package net.bakaar.greetings.stat.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

interface CounterRepository extends ReactiveCrudRepository<Counter, Long> {
    Mono<Counter> findByName(String name);
}
