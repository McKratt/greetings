package net.bakaar.greetings.rest;

import net.bakaar.greetings.application.GreetingApplicationService;
import net.bakaar.greetings.domain.CreateGreetingCommand;
import net.bakaar.greetings.domain.Greeting;
import net.bakaar.greetings.domain.UpdateGreetingCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
        var request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    void createGreeting_should_map_greeting_to_message() {
        // Given
        var name = "name";
        var type = "type";
        var command = new CreateGreetingCommand(type, name);
        var greeting = mock(Greeting.class);
        given(service.createGreeting(any())).willReturn(greeting);
        var message = mock(GreetingMessage.class);
        given(mapper.mapToMessage(greeting)).willReturn(message);
        // When
        var receivedMessage = controller.createGreeting(command).getBody();
        // Then
        verify(mapper).mapToMessage(greeting);
        var captor = ArgumentCaptor.forClass(CreateGreetingCommand.class);
        verify(service).createGreeting(captor.capture());
        var createdCommand = captor.getValue();
        assertThat(createdCommand.name()).isEqualTo(name);
        assertThat(createdCommand.type()).isEqualTo(type);
        assertThat(receivedMessage).isSameAs(message);
    }

    @Test
    void createGreeting_should_set_location_in_header() {
        // Given
        var command = mock(CreateGreetingCommand.class);
        var greeting = mock(Greeting.class);
        given(service.createGreeting(any())).willReturn(greeting);
        var identifier = UUID.randomUUID();
        given(greeting.getIdentifier()).willReturn(identifier);
        // When
        var response = controller.createGreeting(command);
        // Then
        assertThat(response.getHeaders().getLocation().toString()).contains(identifier.toString());
    }

    @Test
    void updateGreeting_should_map_greeting_to_message() {
        // Given
        var type = "type";
        var identifier = UUID.randomUUID();
        var command = new UpdateGreetingCommandDTO();
        command.setNewType(type);
        var greeting = mock(Greeting.class);
        given(service.changeType(any())).willReturn(greeting);
        var message = mock(GreetingMessage.class);
        given(mapper.mapToMessage(greeting)).willReturn(message);
        // When
        var receivedMessage = controller.updateGreeting(identifier, command).getBody();
        // Then
        verify(mapper).mapToMessage(greeting);
        var captor = ArgumentCaptor.forClass(UpdateGreetingCommand.class);
        verify(service).changeType(captor.capture());
        var capturedCommand = captor.getValue();
        assertThat(capturedCommand.identifier()).isSameAs(identifier);
        assertThat(capturedCommand.newType()).isEqualTo(type);
        assertThat(receivedMessage).isSameAs(message);
    }

    @Test
    void read_should_call_application_service() {
        // Given
        var identifier = UUID.randomUUID();
        var greeting = mock(Greeting.class);
        given(service.read(any())).willReturn(greeting);
        // When
        var returnedGreeting = service.read(identifier);
        // Then
        verify(service).read(identifier);
        assertThat(returnedGreeting).isSameAs(greeting);
    }
}