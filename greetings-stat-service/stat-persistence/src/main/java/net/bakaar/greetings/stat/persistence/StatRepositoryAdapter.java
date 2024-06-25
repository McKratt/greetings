package net.bakaar.greetings.stat.persistence;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bakaar.greetings.stat.domain.GreetingsStats;
import net.bakaar.greetings.stat.domain.StatRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatRepositoryAdapter implements StatRepository {

    private final CounterRepository repository;

    @Override
    public void put(GreetingsStats stats) {
        stats.getCounters().forEach((key, value) ->
                repository.findByName(key.toUpperCase(Locale.ROOT))
                        .doOnError(Mono::error)
                        .flatMap(found -> repository.save(found.setCount(value.longValue())))
                        .doOnError(Mono::error)
                        .switchIfEmpty(repository.save(new Counter().setCount(value).setName(key.toUpperCase(Locale.ROOT))))
                        .doOnError(Mono::error)
                        .subscribe());
    }

    @Override
    public CompletableFuture<GreetingsStats> pop() {
        var counters = new HashMap<String, Long>();
        return repository.findAll()
                .doOnNext(counter -> counters.put(counter.getName(), counter.getCount()))
                .then(Mono.just(new GreetingsStats(counters)))
                .toFuture();

    }
}
