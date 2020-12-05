package net.bakaar.greetings.domain;

import net.bakaar.greetings.domain.exception.GreetingMissingNameException;
import net.bakaar.greetings.domain.exception.GreetingMissingTypeException;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class GreetingTest {

    @Test
    void builder_should_build_with_mandatory_fields() {
        // Given
        var name = "Anna";
        // When
        var greeting = Greeting.of("birthday").to(name).build();
        // Then
        assertThat(greeting).isNotNull();
        assertThat(greeting.getType()).isSameAs(GreetingType.BIRTHDAY);
        assertThat(greeting.getMessage()).contains(name);
        assertThat(greeting.getIdentifier()).isNotNull();
    }

    @Test
    void should_not_set_identifier_if_set_in_builder() {
        // Given
        var identifier = UUID.randomUUID();
        // When
        var greeting = Greeting.of("anniversary").to("Chouquette").withIdentifier(identifier.toString()).build();
        // Then
        assertThat(greeting).isNotNull();
        assertThat(greeting.getIdentifier()).isEqualTo(identifier);
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

    @Test
    void getMessage_should_call_the_enum_method() {
        // Given
        final String name = "Nathan";
        Greeting greeting = Greeting.of("Christmas").to(name).build();
        GreetingType spiedType = spy(greeting.getType());
        ReflectionTestUtils.setField(greeting, "type", spiedType);
        // When
        String message = greeting.getMessage();
        // Then
        assertThat(message).isNotEmpty().isNotBlank();
        verify(spiedType).createMessage(name);
    }
}