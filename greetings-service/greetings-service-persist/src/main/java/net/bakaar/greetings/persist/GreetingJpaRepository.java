package net.bakaar.greetings.persist;

import org.springframework.data.repository.CrudRepository;

public interface GreetingJpaRepository extends CrudRepository<GreetingJpaEntity, Long> {
}
