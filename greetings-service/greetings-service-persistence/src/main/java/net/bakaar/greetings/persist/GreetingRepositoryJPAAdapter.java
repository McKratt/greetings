package net.bakaar.greetings.persist;

import lombok.RequiredArgsConstructor;
import net.bakaar.greetings.domain.Greeting;
import net.bakaar.greetings.domain.GreetingRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GreetingRepositoryJPAAdapter implements GreetingRepository {

    private final DomainToEntityMapper mapper;
    private final GreetingJpaRepository repository;

    @Override
    public Greeting put(Greeting greeting) {
        return mapper.mapToDomain(
                repository.save(
                        repository.findByIdentifier(greeting.getIdentifier().toString())
                                .map(jpaEntity -> mapper.mapToEntity(greeting, jpaEntity))
                                .orElseGet(() -> mapper.mapToEntity(greeting))
                )
        );
    }

    @Override
    public Optional<Greeting> find(UUID identifier) {
        return repository.findByIdentifier(identifier.toString()).map(mapper::mapToDomain);
    }
}
