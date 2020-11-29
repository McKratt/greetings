package net.bakaar.greetings.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GreetingsControllerTest {

    @Mock
    private GreetingApplicationService service;

    @InjectMocks
    private GreetingsController controller;

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