package net.bakaar.greetings.stat.persistence;

import net.bakaar.greetings.stat.domain.GreetingsStats;
import net.bakaar.greetings.stat.domain.StatRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

@DataR2dbcTest(properties = {
        "spring.r2dbc.url=r2dbc:h2:mem:///test?options=DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
})
@Import({StatRepositoryAdapter.class})
@ContextConfiguration(classes = StatPersistenceTestApplication.class)
class StatRepositoryAdapterIT {

    @Autowired
    private StatRepository repository;

    @Autowired
    private R2dbcEntityTemplate template;

    @AfterEach
    void tearDown() {
        template.select(Counter.class).all().subscribe(
                counter -> template.delete(counter).doOnError(Mono::error).subscribe()
        );
    }

    @Test
    void put_should_save_all_the_counters() {
        // Arrange
        var stats = new GreetingsStats(new HashMap<>(Map.of("BIRTHDAY", 0L, "ANNIVERSARY", 0L, "CHRISTMAS", 0L)));
        // Act
        repository.put(stats);
        // Assert
        StepVerifier.create(template.select(Counter.class).count())
                .expectNext(3L)
                .verifyComplete();
    }

    @Test
    void should_increase_existing_counter() throws ExecutionException, InterruptedException {
        // Arrange
        var ety = new Counter();
        var name = "BIRTHDAY";
        ety.setName(name);
        var initialCounter = 5L;
        ety.setCount(initialCounter);
        template.insert(ety).block();
        var stats = repository.pop().get();
        assertThat(stats.getStatsFor(name)).contains(initialCounter);
        // Act
        stats = stats.increaseCounterFor(name);
        repository.put(stats);
        // Assert
        StepVerifier.create(template.select(Counter.class).all())
                .expectNextCount(1)
                .assertNext(counter -> {
                    assertThat(counter.getName()).isEqualTo(name);
                    assertThat(counter.getCount()).isEqualTo(initialCounter + 1);
                }).verifyComplete();
    }

    @Test
    void pop_should_get_counter_in_db() throws ExecutionException, InterruptedException {
        // Arrange
        var counter = new Counter();
        var count = 64L;
        var type = "TOTO";
        counter.setCount(count);
        counter.setName(type);
        StepVerifier.create(template.insert(counter))
                .expectNextCount(1L)
                .verifyComplete();
        // Act
        var stats = repository.pop().get();
        // Assert
        assertThat(stats).isNotNull();
        assertThat(stats.getCounters()).hasSize(1);
        assertThat(stats.getStatsFor(type.toUpperCase())).isPresent().get().isEqualTo(count);
    }
}
