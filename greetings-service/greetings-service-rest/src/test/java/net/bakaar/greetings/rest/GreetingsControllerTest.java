package net.bakaar.greetings.rest;

import net.bakaar.greetings.application.GreetingApplicationService;
import net.bakaar.greetings.domain.CreateGreetingCommand;
import net.bakaar.greetings.domain.Greeting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

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

    @BeforeEach
    void setUp() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    void createGreeting_should_map_greeting_to_message() {
        // Given
        var command = mock(CreateGreetingCommand.class);
        var greeting = mock(Greeting.class);
        given(service.createGreeting(command)).willReturn(greeting);
        var message = mock(GreetingMessage.class);
        given(mapper.mapToMessage(greeting)).willReturn(message);
        // When
        var receivedMessage = controller.createGreeting(command).getBody();
        // Then
        verify(mapper).mapToMessage(greeting);
        assertThat(receivedMessage).isSameAs(message);
    }

    @Test
    void createGreeting_should_call_application_service() {
        // Given
        var command = mock(CreateGreetingCommand.class);
        var greeting = mock(Greeting.class);
        given(service.createGreeting(command)).willReturn(greeting);
        var identifier = UUID.randomUUID();
        given(greeting.getIdentifier()).willReturn(identifier);
        // When
        var response = controller.createGreeting(command);
        // Then
        verify(mapper).mapToMessage(greeting);
        assertThat(response.getHeaders().getLocation().toString()).contains(identifier.toString());
    }
}