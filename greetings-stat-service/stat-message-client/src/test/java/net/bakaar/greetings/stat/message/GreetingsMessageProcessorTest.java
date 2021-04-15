package net.bakaar.greetings.stat.message;

import net.bakaar.greetings.stat.message.handler.GreetingMessagePayloadHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.URI;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GreetingsMessageProcessorTest {

    private final GreetingsMessageProcessor processor = new GreetingsMessageProcessor();

    @Mock
    private Acknowledgment ack;

    @Test
    void service_should_choose_the_correct_event_handler() {
        // Given
        var message = mock(GreetingMessage.class);
        var type = URI.create("http://test/events/type1");
        given(message.type()).willReturn(type);
        var payload = "Here is a payload";
        given(message.payload()).willReturn(payload);
        var handler = mock(GreetingMessagePayloadHandler.class);
        // FIXME once lenient BDD is implemented : https://github.com/mockito/mockito/issues/1597
        lenient().when(handler.canHandle(type)).thenReturn(true);
//        given(handler.canHandle(type)).willReturn(true);
        var handler2 = mock(GreetingMessagePayloadHandler.class);
        lenient().when(handler2.canHandle(type)).thenReturn(false);
        // FIXME once lenient BDD is implemented : https://github.com/mockito/mockito/issues/1597
//        given(handler2.canHandle(type)).willReturn(false);
        ReflectionTestUtils.setField(processor, "handlers", Set.of(handler2, handler));
        // When
        processor.processMessage(message, ack);
        // Then
        verify(ack).acknowledge();
        verify(handler).canHandle(type);
        verify(handler).handle(payload);
    }

    @Test
    void should_throw_an_exception_if_no_handler_found() {
        // Given
        var message = mock(GreetingMessage.class);
        var type = URI.create("http://test/events/type1");
        given(message.type()).willReturn(type);
        // When
        var thrown = catchThrowable(() -> processor.processMessage(message, ack));
        // Then
        verify(ack, never()).acknowledge();
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).contains(type.toString());
    }

    @Test
    void should_throw_an_exception_if_no_correct_handler_found() {
        // Given
        var message = mock(GreetingMessage.class);
        var type = URI.create("http://test/events/type1");
        given(message.type()).willReturn(type);
        var handler = mock(GreetingMessagePayloadHandler.class);
        given(handler.canHandle(type)).willReturn(false);
        ReflectionTestUtils.setField(processor, "handlers", Set.of(handler));
        // When
        var thrown = catchThrowable(() -> processor.processMessage(message, ack));
        // Then
        verify(ack, never()).acknowledge();
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).contains(type.toString());
    }
}