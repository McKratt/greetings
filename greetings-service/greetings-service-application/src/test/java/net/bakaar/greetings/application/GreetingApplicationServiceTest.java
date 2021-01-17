package net.bakaar.greetings.application;

import net.bakaar.greetings.application.exception.GreetingNotFoundException;
import net.bakaar.greetings.domain.CreateGreetingCommand;
import net.bakaar.greetings.domain.Greeting;
import net.bakaar.greetings.domain.GreetingRepository;
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
        CreateGreetingCommand command = new CreateGreetingCommand("christmas", "Vivianne");
        Greeting returnedGreeting = service.createGreeting(command);
        // Then
        ArgumentCaptor<Greeting> greetingCaptor = ArgumentCaptor.forClass(Greeting.class);
        verify(repository).put(greetingCaptor.capture());
        final Greeting expected = greetingCaptor.getValue();
        assertThat(returnedGreeting).isSameAs(expected);
    }

    @Test
    void changeType_should_read_repo() {
        // Given
        Greeting greeting = mock(Greeting.class);
        UUID identifier = UUID.randomUUID();
        given(repository.find(identifier)).willReturn(Optional.of(greeting));
        given(repository.put(any())).willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        String type = "type";
        // When
        Greeting returnedGreeting = service.changeType(identifier, type);
        // Then
        assertThat(returnedGreeting).isSameAs(greeting);
        verify(greeting).updateTypeFor(type);
    }

    @Test
    void changeType_should_send_exception_if_not_found() {
        // Given
        UUID identifier = UUID.randomUUID();
        given(repository.find(identifier)).willReturn(Optional.empty());
        // When
        Throwable thrown = catchThrowable(() -> service.changeType(identifier, "birthday"));
        // Then
        assertThat(thrown).isNotNull().isInstanceOf(GreetingNotFoundException.class);
    }
}