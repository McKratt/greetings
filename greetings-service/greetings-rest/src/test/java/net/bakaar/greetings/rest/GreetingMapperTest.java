package net.bakaar.greetings.rest;

import net.bakaar.greetings.domain.Greeting;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GreetingMapperTest {

    private final GreetingMapper mapper = new GreetingMapper();

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