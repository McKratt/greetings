package net.bakaar.greetings.persist;

import net.bakaar.greetings.domain.Greeting;
import net.bakaar.greetings.domain.GreetingType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.transaction.TestTransaction;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // No needs to add @Transactional, @DataJpaTest does that for us.
// needed to include the adapter and the mapper inside the context, because @DataJpaTest restraint the context to only Data concerned beans
@Import({GreetingRepositoryJPAAdapter.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=validate"
})
class GreetingRepositoryJPAAdapterIT {

    @Autowired
    private GreetingRepositoryJPAAdapter adapter;

    @Autowired
    private GreetingJpaRepository repository;
    @Autowired
    private GreetingTypeJpaRepository typeRepository;


    @Test
    void put_should_save_in_db() {
        // Arrange
        var type = "anniversary";
        var name = "Alicia";
        var greeting = Greeting.of(type).to(name).build();
        // Act
        if (!TestTransaction.isActive()) {
            TestTransaction.start();
        }
        TestTransaction.flagForCommit();
        adapter.put(greeting);
        TestTransaction.end();
        // Assert
        var founds = repository.findAll();
        assertThat(founds).isNotEmpty().hasSize(1);
        var saved = founds.iterator().next();
        assertThat(saved.getName()).isEqualTo(name);
        assertThat(saved.getType().getName()).isEqualTo(type.toUpperCase(Locale.ROOT));
        assertThat(saved.getIdentifier()).isEqualTo(greeting.getIdentifier().toString());
    }

    @Test
    void put_should_take_the_existing_one() {
        // Arrange
        var entity = new GreetingJpaEntity();
        var name = "Julie";
        entity.setName(name);
        var identifier = UUID.randomUUID();
        entity.setIdentifier(identifier.toString());
        var creationTime = LocalDateTime.now();
        entity.setCreatedAt(creationTime);
        var typeEntity = typeRepository.findByName(GreetingType.CHRISTMAS.toString()).get();
        entity.setType(typeEntity);

        if (!TestTransaction.isActive()) {
            TestTransaction.start();
        }
        TestTransaction.flagForCommit();
        repository.save(entity);
        TestTransaction.end();
        var newType = GreetingType.ANNIVERSARY;
        var greeting = Greeting.of(newType.toString().toLowerCase(Locale.ROOT)).to(name).withIdentifier(identifier.toString()).build();
        // Act
        TestTransaction.start();
        TestTransaction.flagForCommit();
        adapter.put(greeting);
        TestTransaction.end();
        // Assert
        var founds = repository.findAll();
        assertThat(founds).isNotEmpty().hasSize(1);
        var saved = founds.iterator().next();
        assertThat(saved.getId()).isEqualTo(entity.getId());
        assertThat(saved.getName()).isEqualTo(name);
        assertThat(saved.getType().getName()).isEqualTo(newType.toString().toUpperCase(Locale.ROOT));
        assertThat(saved.getIdentifier()).isEqualTo(identifier.toString());
    }
}
