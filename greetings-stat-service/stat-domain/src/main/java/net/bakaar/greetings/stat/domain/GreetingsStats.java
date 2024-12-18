package net.bakaar.greetings.stat.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class GreetingsStats {
    /**
     * Key should be uppercase.
     */
    private final Map<String, Long> counters;

    public Optional<Long> getStatsFor(String type) {
        return Optional.ofNullable(counters.getOrDefault(type.toUpperCase(Locale.ROOT), 0L));
    }

    public GreetingsStats increaseCounterFor(String type) {
        Optional.ofNullable(counters.computeIfPresent(type.toUpperCase(Locale.ROOT), (currentType, currentValue) -> Long.sum(currentValue, 1L)))
                .orElseGet(() -> counters.put(type.toUpperCase(Locale.ROOT), 1L));
        return this;
    }

    public boolean isEmpty() {
        return counters == null || counters.isEmpty();
    }
}
