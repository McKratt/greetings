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

    public GreetingsStats increaseCounterFor(GreetingType type) {
        counters.compute(type, (currentType, currentValue) -> Long.sum(currentValue, 1L));
        return this;
    }
}
