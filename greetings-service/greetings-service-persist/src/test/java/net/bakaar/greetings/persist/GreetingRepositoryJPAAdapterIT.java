package net.bakaar.greetings.persist;

import net.bakaar.greetings.domain.Greeting;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.transaction.TestTransaction;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
// needed to include the adapter and the mapper inside the context, because @DataJpaTest restraint the context to only Data concerned beans
@Import({GreetingRepositoryJPAAdapter.class, DomainToEntityMapper.class})
class GreetingRepositoryJPAAdapterIT {

    // TODO use TestTransaction.start() .end() .flagForCommit()

    @Autowired
    private GreetingRepositoryJPAAdapter adapter;

    @Autowired
    private GreetingJpaRepository repository;

    @Test
    void put_should_save_in_db() {
        // Given
        var type = "anniversary";
        var name = "Alicia";
        var greeting = Greeting.of(type).to(name).build();
        // When
        if (!TestTransaction.isActive()) {
            TestTransaction.start();
        }
        TestTransaction.flagForCommit();
        adapter.put(greeting);
        TestTransaction.end();
        // Then
        var founds = repository.findAll();
        assertThat(founds).isNotEmpty().hasSize(1);
        var saved = founds.iterator().next();
        assertThat(saved.getName()).isEqualTo(name);
        assertThat(saved.getType()).isEqualTo(type.toUpperCase(Locale.ROOT));
        assertThat(saved.getIdentifier()).isEqualTo(greeting.getIdentifier().toString());
    }


}
