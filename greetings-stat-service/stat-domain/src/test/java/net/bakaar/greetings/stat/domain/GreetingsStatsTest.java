package net.bakaar.greetings.stat.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.HashMap;
import java.util.Map;

import static net.bakaar.greetings.stat.domain.GreetingType.*;
import static org.assertj.core.api.Assertions.assertThat;

class GreetingsStatsTest {

    private final GreetingsStats stats = new GreetingsStats(new HashMap<>(Map.of(BIRTHDAY, 0L, ANNIVERSARY, 0L, CHRISTMAS, 0L)));

    @ParameterizedTest
    @EnumSource(GreetingType.class)
    void should_return_the_corresponding_stat(GreetingType type) {
        // Given
        // When
        var stat = stats.getStatsFor(type);
        // Then
        assertThat(stat).isPresent().get().isEqualTo(0L);
    }

    @ParameterizedTest
    @EnumSource(GreetingType.class)
    void should_increase_the_correct_counter(GreetingType type) {
        // Given
        // When
        var returnedStats = stats.increaseCounterFor(type);
        // Then
        var stat = returnedStats.getStatsFor(type);
        assertThat(stat).isPresent().get().isEqualTo(1L);
    }
}