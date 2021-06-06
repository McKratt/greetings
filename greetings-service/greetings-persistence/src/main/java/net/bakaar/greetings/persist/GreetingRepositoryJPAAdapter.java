package net.bakaar.greetings.persist;

import lombok.RequiredArgsConstructor;
import net.bakaar.greetings.domain.Greeting;
import net.bakaar.greetings.domain.GreetingRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GreetingRepositoryJPAAdapter implements GreetingRepository {

    private final GreetingJpaRepository repository;
    private final GreetingTypeJpaRepository typesRepository;

    @Override
    public Greeting put(Greeting greeting) {
        repository.findByIdentifier(greeting.getIdentifier().toString())
                .map(entity -> {
                    typesRepository.findByName(greeting.getType().name())
                            .ifPresent(entity::setType);
                    return repository.save(entity);
                })
                .orElseGet(() -> {

                    var jpaEntity = new GreetingJpaEntity();
                    jpaEntity.setIdentifier(greeting.getIdentifier().toString());
                    jpaEntity.setName(greeting.getName());
                    typesRepository.findByName(greeting.getType().name())
                            .ifPresent(jpaEntity::setType);
                    jpaEntity.setCreatedAt(LocalDateTime.now());
                    return repository.save(jpaEntity);
                });
        return greeting;
    }

    @Override
    public Optional<Greeting> find(UUID identifier) {
        return repository.findByIdentifier(identifier.toString()).map(entity ->
                Greeting.of(entity.getType().getName())
                        .to(entity.getName())
                        .withIdentifier(entity.getIdentifier())
                        .build());
    }
}
