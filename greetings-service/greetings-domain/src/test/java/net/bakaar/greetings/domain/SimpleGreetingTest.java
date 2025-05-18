package net.bakaar.greetings.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This is a simple test class to demonstrate how to write tests in this project.
 */
class SimpleGreetingTest {

    @Test
    void should_create_greeting_command_with_valid_values() {
        // Arrange
        String type = "birthday";
        String name = "John";

        // Act
        CreateGreetingCommand command = new CreateGreetingCommand(type, name);

        // Assert
        assertThat(command).isNotNull();
        assertThat(command.type()).isEqualTo(type);
        assertThat(command.name()).isEqualTo(name);
    }
}