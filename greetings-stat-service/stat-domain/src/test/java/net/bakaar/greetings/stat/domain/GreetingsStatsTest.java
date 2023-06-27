package net.bakaar.greetings.stat.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GreetingsStatsTest {

    private final GreetingsStats stats = new GreetingsStats(
            new HashMap<>(
                    Map.of("BIRTHDAY", 1L, "ANNIVERSARY", 1L, "CHRISTMAS", 1L)
            ));

    @ParameterizedTest
    @ValueSource(strings = {
            "birthday",
            "Birthday",
            "BIRTHDAY",
            "anniversary",
            "Anniversary",
            "ANNIVERSARY",
            "christmas",
            "Christmas",
            "CHRISTMAS"
    })
    void should_return_the_corresponding_stat(String type) {
        // Arrange
        // Act
        var stat = stats.getStatsFor(type);
        // Assert
        assertThat(stat).isPresent().get().isEqualTo(1L);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "birthday",
            "Birthday",
            "BIRTHDAY",
            "anniversary",
            "Anniversary",
            "ANNIVERSARY",
            "christmas",
            "Christmas",
            "CHRISTMAS",
    })
    void should_increase_the_correct_counter(String type) {
        // Arrange
        // Act
        var returnedStats = stats.increaseCounterFor(type);
        // Assert
        var stat = returnedStats.getStatsFor(type);
        assertThat(stat).isPresent().get().isEqualTo(2L);
    }

    @Test
    void should_return_0_if_the_counter_no_present() {
        // Arrange
        // Act
        var stat = stats.getStatsFor("Easter");
        // Assert
        assertThat(stat).isPresent().get().isEqualTo(0L);
    }

    @Test
    void should_create_and_return_1_if_counter_doesnt_exist() {
        // Arrange
        final String type = "Easter";
        // Act
        var returnedStats = stats.increaseCounterFor(type);
        // Assert
        var stat = returnedStats.getStatsFor(type);
        assertThat(stat).isPresent().get().isEqualTo(1L);
    }
}