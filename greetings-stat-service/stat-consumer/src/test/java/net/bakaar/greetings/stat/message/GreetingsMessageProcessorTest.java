package net.bakaar.greetings.stat.message;

import net.bakaar.greetings.message.GreetingsMessage;
import net.bakaar.greetings.stat.message.handler.GreetingMessagePayloadHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
class GreetingsMessageProcessorTest {

    private final GreetingsMessageProcessor processor = new GreetingsMessageProcessor();

    @Mock
    private Acknowledgment ack;

    @Test
    void service_should_choose_the_correct_event_handler() {
        // Arrange
        var message = mock(GreetingsMessage.class);
        var type = URI.create("http://test/events/type1");
        given(message.type()).willReturn(type);
        var payload = "Here is a payload";
        given(message.payload()).willReturn(payload);
        var handler = mock(GreetingMessagePayloadHandler.class);
        given(handler.canHandle(type)).willReturn(true);
        given(handler.handle(any())).willReturn(Mono.empty());
        var handler2 = mock(GreetingMessagePayloadHandler.class);
        // The handler2 may not be interrogated depending on the order in the Set.
        lenient().when(handler2.canHandle(type)).thenReturn(false);
        // FIXME once lenient BDD is implemented : https://github.com/mockito/mockito/issues/1597
//        given(handler2.canHandle(type)).willReturn(false);
        ReflectionTestUtils.setField(processor, "handlers", Set.of(handler2, handler));
        // Act
        processor.processMessage(message, ack);
        // Assert
        verify(ack).acknowledge();
        verify(handler).canHandle(type);
        verify(handler).handle(payload);
    }

    @Test
    void should_throw_an_exception_if_no_handler_found() {
        // Arrange
        var message = mock(GreetingsMessage.class);
        var type = URI.create("http://test/events/type1");
        given(message.type()).willReturn(type);
        // Act
        var thrown = catchThrowable(() -> processor.processMessage(message, ack));
        // Assert
        verify(ack, never()).acknowledge();
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).contains(type.toString());
    }

    @Test
    void should_throw_an_exception_if_no_correct_handler_found() {
        // Arrange
        var message = mock(GreetingsMessage.class);
        var type = URI.create("http://test/events/type1");
        given(message.type()).willReturn(type);
        var handler = mock(GreetingMessagePayloadHandler.class);
        given(handler.canHandle(type)).willReturn(false);
        ReflectionTestUtils.setField(processor, "handlers", Set.of(handler));
        // Act
        var thrown = catchThrowable(() -> processor.processMessage(message, ack));
        // Assert
        verify(ack, never()).acknowledge();
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).contains(type.toString());
    }

    @Test
    void should_not_ack_if_handler_failed(CapturedOutput output) {
        // Arrange
        var message = mock(GreetingsMessage.class);
        var type = URI.create("http://test/events/type1");
        given(message.type()).willReturn(type);
        var payload = "Here is a payload";
        given(message.payload()).willReturn(payload);
        var handler = mock(GreetingMessagePayloadHandler.class);
        given(handler.canHandle(type)).willReturn(true);
        var exception = mock(RuntimeException.class);
        given(handler.handle(any())).willThrow(exception);
        ReflectionTestUtils.setField(processor, "handlers", Set.of(handler));
        // Act
        var thrown = catchThrowable(() -> processor.processMessage(message, ack));
        // Assert
        verify(ack, never()).acknowledge();
        assertThat(thrown).isSameAs(exception);// the exception is never returned outside the subscriber.
    }
}