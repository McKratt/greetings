package net.bakaar.greetings.stat.persistence;

import net.bakaar.greetings.stat.domain.GreetingsStats;
import net.bakaar.greetings.stat.domain.StatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = StatPersistenceTestConfiguration.class)
class StatRepositoryAdapterIT {

    @Autowired
    private StatRepository repository;

    @Autowired
    private R2dbcEntityTemplate template;

    @Test
    void put_should_save_all_the_counters() {
        // Given
        var stats = new GreetingsStats(new HashMap<>(Map.of("BIRTHDAY", 0L, "ANNIVERSARY", 0L, "CHRISTMAS", 0L)));
        // When
        repository.put(stats);
        // Then
        template.select(Counter.class).count().subscribe(count -> assertThat(count).isEqualTo(3));
    }
}
