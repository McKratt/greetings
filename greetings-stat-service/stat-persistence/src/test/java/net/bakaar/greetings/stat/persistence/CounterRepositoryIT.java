package net.bakaar.greetings.stat.persistence;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = StatPersistenceTestConfiguration.class)
class CounterRepositoryIT {

    @Autowired
    private CounterRepository repository;

    @Test
    void should_begin_empty() {
        // Given
        // When
        repository.findAll()
                .as(StepVerifier::create)
                .expectNextCount(0)
                .verifyComplete();
        // Then
    }


}