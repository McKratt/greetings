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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
        given(statRepository.put(any())).willReturn(CompletableFuture.completedFuture(null));
        var greeting = mock(Greeting.class);
        given(greetingsRepository.getGreetingForIdentifier(identifier)).willReturn(Mono.just(greeting));
        String type = "CHRISTMAS";
        given(greeting.type()).willReturn(type);
        // When
        StepVerifier.create(service.handle(event)).verifyComplete();
        // Then
        verify(statRepository).pop();
        verify(greetingsRepository).getGreetingForIdentifier(identifier);
        verify(stats).increaseCounterFor(type);
        verify(statRepository).put(stats);
    }
}