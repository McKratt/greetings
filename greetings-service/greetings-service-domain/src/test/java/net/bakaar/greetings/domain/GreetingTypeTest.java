package net.bakaar.greetings.domain;

import net.bakaar.greetings.domain.exception.GreetingWrongTypeException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static net.bakaar.greetings.domain.GreetingType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class GreetingTypeTest {


    private static Stream<Arguments> of_should_return_Type() {
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

    private static Stream<Arguments> createMessage_should_return_the_correct_message() {
        return Stream.of(
                arguments(BIRTHDAY, "Robert", "Happy Birthday Robert !"),
                arguments(CHRISTMAS, "Marie", "Merry Christmas Marie !"),
                arguments(ANNIVERSARY, "Paul", "Joyful Anniversary Paul !")
        );
    }

    @ParameterizedTest
    @MethodSource
    void of_should_return_Type(String type, GreetingType expected) {
        // Given
        // When
        GreetingType returnedType = GreetingType.of(type);
        // Then
        assertThat(returnedType).isSameAs(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {"birth", "christ", "bithday"})
    void of_should_throw_exception_if_type_unknown(String type) {
        // Given
        // When
        Throwable thrown = catchThrowable(() -> GreetingType.of(type));
        // Then
        assertThat(thrown).isNotNull().isInstanceOf(GreetingWrongTypeException.class).extracting(Throwable::getMessage).asString().contains(type);
    }

    @ParameterizedTest
    @MethodSource
    void createMessage_should_return_the_correct_message(GreetingType type, String name, String expectedMessage) {
        // Given
        // When
        String message = type.createMessage(name);
        // Then
        assertThat(message).isEqualTo(expectedMessage);
    }
}