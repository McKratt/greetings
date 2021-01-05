package net.bakaar.greetings.persist;

import lombok.RequiredArgsConstructor;
import net.bakaar.greetings.domain.Greeting;
import net.bakaar.greetings.domain.GreetingRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GreetingRepositoryJPAAdapter implements GreetingRepository {

    private final DomainToEntityMapper mapper;
    private final GreetingJpaRepository repository;

    @Override
    public Greeting put(Greeting greeting) {
        //TODO look if the greeting already exist to handle update operation
        return mapper.mapToDomain(repository.save(mapper.mapToEntity(greeting)));
    }
}
