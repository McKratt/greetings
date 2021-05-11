package net.bakaar.greetings.persist;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GreetingJpaRepository extends CrudRepository<GreetingJpaEntity, Long> {
    Optional<GreetingJpaEntity> findByIdentifier(String identifier);

}
