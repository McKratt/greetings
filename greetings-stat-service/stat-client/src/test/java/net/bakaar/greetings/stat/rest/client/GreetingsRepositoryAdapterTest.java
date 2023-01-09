package net.bakaar.greetings.stat.rest.client;

import net.bakaar.greetings.stat.application.readmodel.Greeting;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GreetingsRepositoryAdapterTest {

    @Mock
    private WebClient client;

    @Mock
    private GreetingsStatRestClientProperties properties;

    @InjectMocks
    private GreetingsRepositoryAdapter adapter;

    @Test
    void should_call_the_client_with_correct_url() {
        // Given
        var identifier = UUID.randomUUID();
        var greeting = mock(Greeting.class);

        var requestHeaderUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        given(client.get()).willReturn(requestHeaderUriSpec);
        var requestHeaderSpec = mock(WebClient.RequestHeadersSpec.class);
        given(requestHeaderUriSpec.uri(anyString(), anyString())).willReturn(requestHeaderSpec);
        given(requestHeaderSpec.accept(any())).willReturn(requestHeaderSpec);
        var responseSpec = mock(WebClient.ResponseSpec.class);
        given(requestHeaderSpec.retrieve()).willReturn(responseSpec);
        given(responseSpec.bodyToMono(any(Class.class))).willReturn(Mono.just(greeting));
        // When
        StepVerifier.create(adapter.getGreetingForIdentifier(identifier))
                .expectNext(greeting)
                .verifyComplete();
        // Then
        verify(requestHeaderSpec).accept(MediaType.APPLICATION_JSON);
        verify(requestHeaderUriSpec).uri("/rest/api/v1/greetings/{id}", identifier.toString());
        verify(responseSpec).bodyToMono(Greeting.class);
    }
}