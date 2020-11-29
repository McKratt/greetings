package net.bakaar.greetings.rest;

import net.bakaar.greetings.application.GreetingApplicationService;
import net.bakaar.greetings.rest.model.GreetingMessage;
import net.bakaar.grettings.domain.CreateGreetingCommand;
import net.bakaar.grettings.domain.Greeting;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GreetingsControllerTest {

    @Mock
    private GreetingApplicationService service;

    @Mock
    private GreetingToMessageMapper mapper;

    @InjectMocks
    private GreetingsController controller;

    @Test
    void createGreeting_should_map_greeting_to_message() {
        // Given
        CreateGreetingCommand command = mock(CreateGreetingCommand.class);
        Greeting greeting = mock(Greeting.class);
        given(service.createGreeting(command)).willReturn(greeting);
        GreetingMessage message = mock(GreetingMessage.class);
        given(mapper.mapToMessage(greeting)).willReturn(message);
        // When
        GreetingMessage receivedMessage = controller.createGreeting(command);
        // Then
        verify(mapper).mapToMessage(greeting);
        assertThat(receivedMessage).isSameAs(message);
    }

    @Test
    void createGreeting_should_call_application_service() {
        // Given
        CreateGreetingCommand command = mock(CreateGreetingCommand.class);
        // When
        controller.createGreeting(command);
        // Then
        verify(service).createGreeting(command);
    }
}