package net.bakaar.greetings.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class CreateGreetingCommandTest {
    @Test
    void should_throw_exception_if_type_is_null() {
        // Arrange
        // Act
        var thrown = catchThrowable(() -> new CreateGreetingCommand(null, "name"));
        // Assert
        assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessageContaining("Type");
    }

    @Test
    void should_throw_exception_if_name_is_null() {
        // Arrange
        // Act
        var thrown = catchThrowable(() -> new CreateGreetingCommand("type", null));
        // Assert
        assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessageContaining("Name");
    }
}