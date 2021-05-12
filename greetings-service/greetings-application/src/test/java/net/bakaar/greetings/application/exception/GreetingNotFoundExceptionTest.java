package net.bakaar.greetings.application.exception;

import org.junit.jupiter.api.Test;

class GreetingNotFoundExceptionTest {

    @Test
    void should_set_message() {
        // Given
        var message = "toto";
        // When
        var exception = new GreetingNotFoundException(message);
        // Then
        assertThat(exception).extracting(Throwable::getMessage).isEqualTo(message);
    }
}