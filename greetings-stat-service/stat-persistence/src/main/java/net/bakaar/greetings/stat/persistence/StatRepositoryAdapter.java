package net.bakaar.greetings.stat.persistence;

import lombok.RequiredArgsConstructor;
import net.bakaar.greetings.stat.domain.GreetingsStats;
import net.bakaar.greetings.stat.domain.StatRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StatRepositoryAdapter implements StatRepository {

    private final CounterRepository repository;

    @Override
    public CompletableFuture<Void> put(GreetingsStats stats) {
        repository.saveAll(stats.getCounters()
                .entrySet().stream()
                .map(entry -> new Counter().setName(entry.getKey().toUpperCase(Locale.ROOT)).setCount(entry.getValue()))
                .collect(Collectors.toList()));
        // FIXME there should be a way to do that better...
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<GreetingsStats> pop() {
        var counters = new HashMap<String, Long>();
        repository.findAll().subscribe(counter -> counters.put(counter.getName(), counter.getCount()));
        return CompletableFuture.completedFuture(new GreetingsStats(counters));
    }
}
