package net.bakaar.greetings.domain;

import net.bakaar.greetings.domain.exception.GreetingMissingNameException;
import net.bakaar.greetings.domain.exception.GreetingMissingTypeException;
import net.bakaar.greetings.domain.exception.GreetingUnmodifiableTypeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;
import java.util.stream.Stream;

import static net.bakaar.greetings.domain.GreetingType.ANNIVERSARY;
import static net.bakaar.greetings.domain.GreetingType.BIRTHDAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class GreetingTest {

    static Stream<Arguments> updateType_should_change_the_type() {
        return Stream.of(
                arguments("anniversary", "birthday", BIRTHDAY),
                arguments("birthday", "anniversary", ANNIVERSARY)
        );
    }

    @Test
    void builder_should_build_with_mandatory_fields() {
        // Arrange
        var name = "Anna";
        // Act
        var greeting = Greeting.of("birthday").to(name).build();
        // Assert
        assertThat(greeting).isNotNull();
        assertThat(greeting.getType()).isSameAs(BIRTHDAY);
        assertThat(greeting.getMessage()).contains(name);
        assertThat(greeting.getIdentifier()).isNotNull();
    }

    @Test
    void should_not_set_identifier_if_set_in_builder() {
        // Arrange
        var identifier = UUID.randomUUID();
        // Act
        var greeting = Greeting.of("anniversary").to("Chouquette").withIdentifier(identifier.toString()).build();
        // Assert
        assertThat(greeting).isNotNull();
        assertThat(greeting.getIdentifier()).isEqualTo(identifier);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    void builder_should_set_identifier_if_empty(String identifier) {
        // Arrange
        // Act
        var greeting = Greeting.of("anniversary").to("Chouquette").withIdentifier(identifier).build();
        // Assert
        assertThat(greeting).isNotNull();
        assertThat(greeting.getIdentifier()).isNotNull();
    }

    @Test
    void builder_should_throw_exception_if_name_null() {
        // Arrange
        // Act
        var thrown = catchThrowable(() -> Greeting.of("christmas").to(null).build());
        // Assert
        assertThat(thrown).isNotNull().isInstanceOf(GreetingMissingNameException.class);
    }

    @Test
    void builder_should_throw_exception_if_type_null() {
        // Arrange
        // Act
        var thrown = catchThrowable(() -> Greeting.of(null).to("Theo").build());
        // Assert
        assertThat(thrown).isNotNull().isInstanceOf(GreetingMissingTypeException.class);
    }

    @Test
    void getMessage_should_call_the_enum_method() {
        // Arrange
        var name = "Nathan";
        var greeting = Greeting.of("Christmas").to(name).build();
        var spiedType = spy(greeting.getType());
        ReflectionTestUtils.setField(greeting, "type", spiedType);
        // Act
        var message = greeting.getMessage();
        // Assert
        assertThat(message).isNotEmpty().isNotBlank();
        verify(spiedType).createMessage(name);
    }

    @ParameterizedTest
    @MethodSource
    void updateType_should_change_the_type(String firstType, String newType, GreetingType updatedType) {
        // Arrange
        var greeting = Greeting.of(firstType).to("Chouquette").build();
        // Act
        greeting.updateTypeFor(newType);
        // Assert
        assertThat(greeting.getType()).isSameAs(updatedType);
    }

    @ParameterizedTest
    @ValueSource(strings = {"birthday", "anniversary"})
    void updateType_should_not_change_the_type_if_previous_type_is_not_correct(String newType) {
        // Arrange
        var greeting = Greeting.of("christmas").to("Babette").build();
        // Act
        var thrown = catchThrowable(() -> greeting.updateTypeFor(newType));
        // Assert
        assertThat(thrown).isNotNull().isInstanceOf(GreetingUnmodifiableTypeException.class);
    }
}