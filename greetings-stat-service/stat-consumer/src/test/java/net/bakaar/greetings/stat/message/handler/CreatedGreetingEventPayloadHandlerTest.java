package net.bakaar.greetings.stat.message.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bakaar.greetings.stat.application.StatApplicationService;
import net.bakaar.greetings.stat.domain.GreetingCreated;
import net.bakaar.greetings.stat.message.exception.JsonDeserializationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreatedGreetingEventPayloadHandlerTest {

    @Mock
    private StatApplicationService service;

    @Mock
    private ObjectMapper jsonMapper;

    @InjectMocks
    private CreatedGreetingEventPayloadHandler handler;

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "https://google.com",
            "https://bakaar.net/greetings/events/greeting-createds",
            "http://bakaar.net/greetings/events/greeting-created/1",
            "http://bakaar.net/greetings/events/greeting-created"
    })
    void canHandle_should_return_false(String type) {
        // Arrange
        // Act
        assertThat(handler.canHandle(URI.create(type))).isFalse();
        // Assert
    }

    @Test
    void canHandle_should_return_true() {
        assertThat(handler.canHandle(URI.create("https://bakaar.net/greetings/events/greeting-created"))).isTrue();
    }

    @Test
    void handle_should_call_jsonMapper_and_service() throws JsonProcessingException {
        // Arrange
        var payload = "Payload";
        var event = mock(GreetingCreated.class);
        given(jsonMapper.readValue(payload, GreetingCreated.class)).willReturn(event);
        given(service.handle(any())).willReturn(Mono.empty());
        // Act
        StepVerifier.create(handler.handle(payload))
                .expectComplete()
                .verify();
        // Assert
        verify(jsonMapper).readValue(payload, GreetingCreated.class);
        verify(service).handle(event);
    }

    @Test
    void handle_should_throw_exception() throws JsonProcessingException {
        // Arrange
        var cause = new JsonEOFException(null, null, null);
        given(jsonMapper.readValue(anyString(), any(Class.class))).willThrow(cause);
        // Act
        StepVerifier.create(handler.handle("Whatever"))
                .expectErrorMatches(throwable -> throwable instanceof JsonDeserializationException &&
                        throwable.getCause().equals(cause))
                .verify();
    }
}