package net.bakaar.greetings.persist;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GreetingTypeJpaRepository extends CrudRepository<GreetingTypeJpaEntity, Long> {
    Optional<GreetingTypeJpaEntity> findByName(String name);
}
