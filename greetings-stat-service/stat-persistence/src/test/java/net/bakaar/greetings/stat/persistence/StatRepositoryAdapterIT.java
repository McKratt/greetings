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
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DataR2dbcTest
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
                counter -> template.delete(counter).subscribe()
        );
    }

    @Test
    void put_should_save_all_the_counters() {
        // Given
        var stats = new GreetingsStats(new HashMap<>(Map.of("BIRTHDAY", 0L, "ANNIVERSARY", 0L, "CHRISTMAS", 0L)));
        // When
        repository.put(stats);
        // Then
        StepVerifier.create(template.select(Counter.class).count())
                .expectNext(3L)
                .verifyComplete();
    }

    @Test
    void pop_should_get_counter_in_db() {
        // Given
        var counter = new Counter();
        var count = 64L;
        var type = "TOTO";
        counter.setCount(count);
        counter.setName(type);
        StepVerifier.create(template.insert(counter))
                .expectNextCount(1L)
                .verifyComplete();
        // When
//        await().until();
        var stats = repository.pop();
        // Then
        assertThat(stats).isNotNull();
        assertThat(stats.getCounters()).hasSize(1);
        assertThat(stats.getStatsFor(type.toUpperCase())).isPresent().get().isEqualTo(count);
    }
}
