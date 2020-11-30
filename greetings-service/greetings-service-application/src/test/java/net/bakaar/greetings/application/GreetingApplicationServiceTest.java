package net.bakaar.greetings.application;

import net.bakaar.greetings.domain.CreateGreetingCommand;
import net.bakaar.greetings.domain.Greeting;
import net.bakaar.greetings.domain.GreetingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
}