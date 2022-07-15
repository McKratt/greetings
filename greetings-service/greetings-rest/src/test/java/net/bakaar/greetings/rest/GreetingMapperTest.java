package net.bakaar.greetings.rest;

import net.bakaar.greetings.domain.Greeting;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GreetingMapperTest {

    private final GreetingMapper mapper = new GreetingMapper();

    @Test
    void mapToMessage_should_getMessage() {
        // Given
        var expectedMessage = "My Expected Message";
        var greeting = mock(Greeting.class);
        given(greeting.getMessage()).willReturn(expectedMessage);
        // When
        var receivedMessage = mapper.mapToMessage(greeting);
        // Then
        assertThat(receivedMessage).isNotNull()
                .extracting(GreetingMessage::message).isEqualTo(expectedMessage);
    }

    @Test
    void mapToIdentifiedMessage_should_getMessage() {
        // Given
        var expectedMessage = "My Expected Message";
        var greeting = mock(Greeting.class);
        given(greeting.getMessage()).willReturn(expectedMessage);
        var identifier = UUID.randomUUID();
        given(greeting.getIdentifier()).willReturn(identifier);
        // When
        var receivedMessage = mapper.mapToIdentifiedMessage(greeting);
        // Then
        assertThat(receivedMessage).isNotNull()
                .extracting(IdentifiedGreetingMessage::message, IdentifiedGreetingMessage::id).isEqualTo(List.of(expectedMessage, identifier.toString()));
    }

    @Test
    void mapToJson_should_map_fields() {
        // Given
        var name = "TestName";
        var greeting = Greeting.of("Anniversary").to(name).build();
        // When
        var json = mapper.mapToJson(greeting);
        // Then
        assertThat(json).isNotNull();
        assertThat(json.name()).isEqualTo(name);
        assertThat(json.type()).isEqualTo("ANNIVERSARY");
    }
}