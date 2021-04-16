package net.bakaar.greetings.stat.domain;

import net.bakaar.greetings.stat.domain.exception.GreetingWrongTypeException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static net.bakaar.greetings.stat.domain.GreetingType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class GreetingTypeTest {

    private static Stream<Arguments> of_should_return_type() {
        return Stream.of(
                arguments("birthday", BIRTHDAY),
                arguments("Birthday", BIRTHDAY),
                arguments("BirthDay", BIRTHDAY),
                arguments("BIRTHDAY", BIRTHDAY),
                arguments("anniversary", ANNIVERSARY),
                arguments("Anniversary", ANNIVERSARY),
                arguments("ANNIVERSARY", ANNIVERSARY),
                arguments("christmas", CHRISTMAS),
                arguments("Christmas", CHRISTMAS),
                arguments("CHRISTMAS", CHRISTMAS)
        );
    }

    @ParameterizedTest
    @MethodSource
    void of_should_return_type(String type, GreetingType expected) {
        // Given
        // When
        var returnedType = GreetingType.of(type);
        // Then
        assertThat(returnedType).isSameAs(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {"birth", "christ", "bithday"})
    void of_should_throw_exception_if_type_unknown(String type) {
        // Given
        // When
        var thrown = catchThrowable(() -> GreetingType.of(type));
        // Then
        assertThat(thrown).isNotNull().isInstanceOf(GreetingWrongTypeException.class).extracting(Throwable::getMessage).asString().contains(type);
    }

}