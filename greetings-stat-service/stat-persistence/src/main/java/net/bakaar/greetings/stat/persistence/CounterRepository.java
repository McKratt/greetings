package net.bakaar.greetings.stat.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CounterRepository extends ReactiveCrudRepository<Counter, Long> {
}
