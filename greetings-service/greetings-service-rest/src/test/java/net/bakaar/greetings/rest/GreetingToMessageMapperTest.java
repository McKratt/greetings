package net.bakaar.greetings.rest;

import net.bakaar.greetings.domain.Greeting;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GreetingToMessageMapperTest {

    private final GreetingToMessageMapper mapper = new GreetingToMessageMapper();

    @Test
    void mapToMessage_should_getMessage() {
        // Given
        String expectedMessage = "My Expected Message";
        Greeting greeting = mock(Greeting.class);
        given(greeting.getMessage()).willReturn(expectedMessage);
        // When
        GreetingMessage receivedMessage = mapper.mapToMessage(greeting);
        // Then
        assertThat(receivedMessage).isNotNull()
                .extracting(GreetingMessage::message).isEqualTo(expectedMessage);
    }
}