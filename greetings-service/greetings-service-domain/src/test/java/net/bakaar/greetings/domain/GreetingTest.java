package net.bakaar.greetings.domain;

import net.bakaar.greetings.domain.exception.GreetingMissingNameException;
import net.bakaar.greetings.domain.exception.GreetingMissingTypeException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class GreetingTest {

    @Test
    void builder_should_build_with_mandatory_fields() {
        // Given
        final String name = "Anna";
        // When
        Greeting greeting = Greeting.of("birthday").to(name).build();
        // Then
        assertThat(greeting).isNotNull();
        assertThat(greeting.getType()).isSameAs(GreetingType.BIRTHDAY);
        assertThat(greeting.getMessage()).contains(name);
        assertThat(greeting.getIdentifier()).isNotNull();
    }

    @Test
    void builder_should_throw_exception_if_name_null() {
        // Given
        // When
        Throwable thrown = catchThrowable(() -> Greeting.of("christmas").to(null).build());
        // Then
        assertThat(thrown).isNotNull().isInstanceOf(GreetingMissingNameException.class);
    }

    @Test
    void builder_should_throw_exception_if_type_null() {
        // Given
        // When
        Throwable thrown = catchThrowable(() -> Greeting.of(null).to("Theo").build());
        // Then
        assertThat(thrown).isNotNull().isInstanceOf(GreetingMissingTypeException.class);
    }
}