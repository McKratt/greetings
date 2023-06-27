package net.bakaar.greetings.application.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GreetingNotFoundExceptionTest {

    @Test
    void should_set_message() {
        // Arrange
        var message = "toto";
        // Act
        var exception = new GreetingNotFoundException(message);
        // Assert
        assertThat(exception).extracting(Throwable::getMessage).isEqualTo(message);
    }
}