package net.bakaar.greetings.application;

import net.bakaar.greetings.application.exception.GreetingNotFoundException;
import net.bakaar.greetings.domain.CreateGreetingCommand;
import net.bakaar.greetings.domain.Greeting;
import net.bakaar.greetings.domain.GreetingRepository;
import net.bakaar.greetings.domain.UpdateGreetingCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GreetingApplicationServiceTest {

    @Mock
    private GreetingRepository repository;

    @InjectMocks
    private GreetingApplicationService service;

    @Test
    void create_should_call_repository() {
        // Given
        given(repository.put(any())).willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        // When
        var command = new CreateGreetingCommand("christmas", "Vivianne");
        var returnedGreeting = service.createGreeting(command);
        // Then
        var greetingCaptor = ArgumentCaptor.forClass(Greeting.class);
        verify(repository).put(greetingCaptor.capture());
        final Greeting expected = greetingCaptor.getValue();
        assertThat(returnedGreeting).isSameAs(expected);
    }

    @Test
    void changeType_should_read_repo() {
        // Given
        var greeting = mock(Greeting.class);
        var identifier = UUID.randomUUID();
        given(repository.find(identifier)).willReturn(Optional.of(greeting));
        given(repository.put(any())).willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        var type = "type";
        var command = new UpdateGreetingCommand(identifier, type);
        // When
        var returnedGreeting = service.changeType(command);
        // Then
        assertThat(returnedGreeting).isSameAs(greeting);
        verify(greeting).updateTypeFor(type);
    }

    @Test
    void changeType_should_send_exception_if_not_found() {
        // Given
        var identifier = UUID.randomUUID();
        given(repository.find(identifier)).willReturn(Optional.empty());
        var command = new UpdateGreetingCommand(identifier, "birthday");
        // When
        var thrown = catchThrowable(() -> service.changeType(command));
        // Then
        assertThat(thrown).isNotNull().isInstanceOf(GreetingNotFoundException.class);
    }
}