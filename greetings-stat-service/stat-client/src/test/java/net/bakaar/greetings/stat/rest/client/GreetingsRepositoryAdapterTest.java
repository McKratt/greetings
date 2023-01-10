package net.bakaar.greetings.stat.rest.client;

import net.bakaar.greetings.stat.application.readmodel.Greeting;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GreetingsRepositoryAdapterTest {

    @Mock
    private GreetingsRestClient client;

    @InjectMocks
    private GreetingsRepositoryAdapter adapter;

    @Test
    void should_call_the_client_with_correct_url() {
        // Arrange
        var identifier = UUID.randomUUID();
        var greeting = mock(Greeting.class);
        given(client.findGreetingFromId(any())).willReturn(Mono.just(greeting));
        // Act
        StepVerifier.create(adapter.getGreetingForIdentifier(identifier))
                .expectNext(greeting)
                .verifyComplete();
        // Assert
        verify(client).findGreetingFromId(identifier);
    }
}