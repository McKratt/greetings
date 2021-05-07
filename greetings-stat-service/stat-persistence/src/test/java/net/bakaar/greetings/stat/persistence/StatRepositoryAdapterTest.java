package net.bakaar.greetings.stat.persistence;

import net.bakaar.greetings.stat.domain.GreetingsStats;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StatRepositoryAdapterTest {
    @Mock
    private CounterRepository repository;

    @InjectMocks
    private StatRepositoryAdapter adapter;

    @Test
    void should_save_with_names_uppercase() {
        // Given
        var stats = mock(GreetingsStats.class);
        given(stats.getCounters()).willReturn(Map.of("birthday", 0L));
        given(repository.saveAll(any(Iterable.class))).willAnswer(invocation -> Flux.fromIterable(invocation.getArgument(0)));
        // When
        adapter.put(stats);
        // Then
        var captor = ArgumentCaptor.forClass(Iterable.class);
        verify(repository).saveAll(captor.capture());
        var counter = (Counter) captor.getValue().iterator().next();
        assertThat(counter).isNotNull();
        assertThat(counter.getName()).isEqualTo("BIRTHDAY");
    }

    @Test
    void should_save_all_the_counters() {
        // Given
        var stats = mock(GreetingsStats.class);
        given(stats.getCounters()).willReturn(Map.of("birthday", 0L, "anniversary", 0L));
        given(repository.saveAll(any(Iterable.class))).willAnswer(invocation -> Flux.fromIterable(invocation.getArgument(0)));
        // When
        adapter.put(stats);
        // Then
        var captor = ArgumentCaptor.forClass(Iterable.class);
        verify(repository).saveAll(captor.capture());
        var list = captor.getValue();
        assertThat(list).isNotNull().hasSize(2);
    }

    @Test
    void should_pop_all_the_existing_counter() {
        // Given
        var key1 = "key1";
        var key2 = "key2";
        var key3 = "key3";
        given(repository.findAll()).willReturn(Flux.just(new Counter().setName(key1), new Counter().setName(key2), new Counter().setName(key3)));
        // When
        var stats = adapter.pop();
        // Then
        assertThat(stats.getCounters()).containsKeys(key1, key2, key3);
    }
}
