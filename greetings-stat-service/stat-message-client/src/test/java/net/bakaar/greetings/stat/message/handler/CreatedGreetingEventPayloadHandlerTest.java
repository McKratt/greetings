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

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
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
            "http://bakaar.net/greetings/events/greeting-createds",
            "http://bakaar.net/greetings/events/greeting-created/1"
    })
    void canHandle_should_return_false(String type) {
        // Given
        // When
        assertThat(handler.canHandle(URI.create(type))).isFalse();
        // Then
    }

    @Test
    void canHandle_should_return_true() {
        assertThat(handler.canHandle(URI.create("http://bakaar.net/greetings/events/greeting-created"))).isTrue();
    }

    @Test
    void handle_should_call_jsonMapper_and_service() throws JsonProcessingException {
        // Given
        var payload = "Payload";
        var event = mock(GreetingCreated.class);
        given(jsonMapper.readValue(payload, GreetingCreated.class)).willReturn(event);
        // When
        handler.handle(payload);
        // Then
        verify(jsonMapper).readValue(payload, GreetingCreated.class);
        verify(service).handle(event);
    }

    @Test
    void handle_should_throw_exception() throws JsonProcessingException {
        // Given
        var cause = new JsonEOFException(null, null, null);
        given(jsonMapper.readValue(anyString(), any(Class.class))).willThrow(cause);
        // When
        Throwable thrown = catchThrowable(() -> handler.handle("Whatever"));
        // Then
        assertThat(thrown).isInstanceOf(JsonDeserializationException.class);
        assertThat(thrown.getCause()).isSameAs(cause);
    }
}