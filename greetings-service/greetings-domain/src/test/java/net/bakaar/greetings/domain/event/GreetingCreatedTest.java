package net.bakaar.greetings.domain.event;

import net.bakaar.greetings.domain.Greeting;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GreetingCreatedTest {

    @Test
    void should_get_identifier_from_greeting() {
        // Given
        var greeting = mock(Greeting.class);
        var identifier = UUID.randomUUID();
        given(greeting.getIdentifier()).willReturn(identifier);
        // When
        var event = GreetingCreated.of(greeting);
        // Then
        assertThat(event).isNotNull();
        assertThat(event.getIdentifier()).isEqualTo(identifier);
    }
}