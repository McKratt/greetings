package net.bakaar.greetings.stat.persistence;

import net.bakaar.greetings.stat.domain.GreetingsStats;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatRepositoryAdapterTest {
    @Mock
    private CounterRepository repository;

    @InjectMocks
    private StatRepositoryAdapter adapter;

    @Test
    void should_save_with_names_uppercase() {
        // Arrange
        var stats = mock(GreetingsStats.class);
        given(stats.getCounters()).willReturn(Map.of("birthday", 0L));
        given(repository.findByName(anyString())).willReturn(Mono.empty());
        given(repository.save(any(Counter.class))).willAnswer(invocationOnMock -> Mono.just(invocationOnMock.getArgument(0)));
        // Act
        adapter.put(stats);
        // Assert
        var captor = ArgumentCaptor.forClass(Counter.class);
        verify(repository).save(captor.capture());
        var counter = captor.getValue();
        assertThat(counter).isNotNull();
        assertThat(counter.getName()).isEqualTo("BIRTHDAY");
    }

    @Test
    void should_save_all_the_counters() {
        // Arrange
        var stats = mock(GreetingsStats.class);
        given(stats.getCounters()).willReturn(Map.of("birthday", 0L, "anniversary", 0L));
        given(repository.findByName(anyString())).willReturn(Mono.empty());
        given(repository.save(any(Counter.class))).willAnswer(invocationOnMock -> Mono.just(invocationOnMock.getArgument(0)));
        // Act
        adapter.put(stats);
        // Assert
        var captor = ArgumentCaptor.forClass(Counter.class);
        verify(repository, times(2)).save(captor.capture());
        var list = captor.getAllValues();
        assertThat(list).isNotNull().hasSize(2);
    }

    @Test
    void should_pop_all_the_existing_counter() throws ExecutionException, InterruptedException {
        // Arrange
        var key1 = "key1";
        var key2 = "key2";
        var key3 = "key3";
        given(repository.findAll()).willReturn(Flux.just(new Counter().setName(key1), new Counter().setName(key2), new Counter().setName(key3)));
        // Act
        var stats = adapter.pop().get();
        // Assert
        assertThat(stats.getCounters()).containsKeys(key1, key2, key3);
    }
}
