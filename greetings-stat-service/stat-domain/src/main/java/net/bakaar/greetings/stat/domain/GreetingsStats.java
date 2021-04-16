package net.bakaar.greetings.stat.domain;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class GreetingsStats {
    private final Map<GreetingType, Long> counters;

    public Optional<Long> getStatsFor(GreetingType type) {
        return Optional.ofNullable(counters.getOrDefault(type, null));
    }

    public void increaseCounterFor(GreetingType type) {
        counters.compute(type, (currentType, currentValue) -> currentValue + 1L);
    }
}
