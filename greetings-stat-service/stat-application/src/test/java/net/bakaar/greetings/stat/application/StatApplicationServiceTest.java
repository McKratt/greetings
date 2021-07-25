package net.bakaar.greetings.stat.application;

import net.bakaar.greetings.stat.application.readmodel.Greeting;
import net.bakaar.greetings.stat.domain.GreetingCreated;
import net.bakaar.greetings.stat.domain.GreetingsStats;
import net.bakaar.greetings.stat.domain.StatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatApplicationServiceTest {
    @Mock
    private StatRepository statRepository;
    @Mock
    private GreetingsRepository greetingsRepository;
    @InjectMocks
    private StatApplicationService service;

    @Test
    void should_call_repositories() {
        // Given
        var identifier = UUID.randomUUID();
        var event = mock(GreetingCreated.class);
        given(event.identifier()).willReturn(identifier);
        var stats = mock(GreetingsStats.class);
        given(stats.increaseCounterFor(any())).willReturn(stats);
        given(statRepository.pop()).willReturn(CompletableFuture.completedFuture(stats));
        var greeting = mock(Greeting.class);
        given(greetingsRepository.getGreetingForIdentifier(any())).willReturn(Mono.just(greeting));
        var type = "CHRISTMAS";
        given(greeting.type()).willReturn(type);
        // When
        StepVerifier.create(service.handle(event)).verifyComplete();
        // Then
        verify(statRepository).pop();
        verify(greetingsRepository).getGreetingForIdentifier(identifier);
        verify(stats).increaseCounterFor(type);
        verify(statRepository).put(stats);
    }

    @Test
    void should_call_repository() {
        // Given
        var stats = mock(GreetingsStats.class);
        given(statRepository.pop()).willReturn(CompletableFuture.completedFuture(stats));
        // When
        StepVerifier.create(service.retrieveGreetingsStats())
                .expectNext(stats)
                .verifyComplete();
        // Then
        verify(statRepository).pop();
    }

    @Test
    void should_propagate_exception_when_pop_failed() {
        // Given
        var exception = mock(RuntimeException.class);
        given(statRepository.pop()).willThrow(exception);
        // When
        StepVerifier.create(service.retrieveGreetingsStats())
                .expectErrorSatisfies((ex) -> assertThat(ex).isSameAs(exception))
                .verify();
        // Then
        verify(statRepository).pop();
    }

    @Test
    void should_propagate_exception_when_put_failed() {
        // Given
        var identifier = UUID.randomUUID();
        var event = mock(GreetingCreated.class);
        given(event.identifier()).willReturn(identifier);
        var stats = mock(GreetingsStats.class);
        given(stats.increaseCounterFor(any())).willReturn(stats);
        given(statRepository.pop()).willReturn(CompletableFuture.completedFuture(stats));
        var greeting = mock(Greeting.class);
        given(greetingsRepository.getGreetingForIdentifier(any())).willReturn(Mono.just(greeting));
        var type = "CHRISTMAS";
        given(greeting.type()).willReturn(type);
        var exception = mock(RuntimeException.class);
        doThrow(exception).when(statRepository).put(any());
        // When
        StepVerifier.create(service.handle(event))
                .expectErrorSatisfies((ex) -> assertThat(ex).isSameAs(exception))
                .verify();
        // Then
        verify(statRepository).pop();
        verify(greetingsRepository).getGreetingForIdentifier(identifier);
        verify(stats).increaseCounterFor(type);
        verify(statRepository).put(stats);
    }
}