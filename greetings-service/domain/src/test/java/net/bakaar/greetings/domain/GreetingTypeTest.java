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

    private static Stream<Arguments> createMessage_should_return_the_correct_message() {
        return Stream.of(
                arguments(BIRTHDAY, "Robert", "Happy Birthday Robert !"),
                arguments(CHRISTMAS, "Marie", "Merry Christmas Marie !"),
                arguments(ANNIVERSARY, "Paul", "Joyful Anniversary Paul !")
        );
    }

    private static Stream<Arguments> canBeChangedFor_should_tell_us_if_type_are_compatible() {
        return Stream.of(
                arguments(BIRTHDAY, ANNIVERSARY, true),
                arguments(BIRTHDAY, BIRTHDAY, true),
                arguments(BIRTHDAY, CHRISTMAS, false),
                arguments(ANNIVERSARY, ANNIVERSARY, true),
                arguments(ANNIVERSARY, BIRTHDAY, true),
                arguments(ANNIVERSARY, CHRISTMAS, false),
                arguments(CHRISTMAS, ANNIVERSARY, false),
                arguments(CHRISTMAS, BIRTHDAY, false),
                arguments(CHRISTMAS, CHRISTMAS, false)

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

    @ParameterizedTest
    @MethodSource
    void createMessage_should_return_the_correct_message(GreetingType type, String name, String expectedMessage) {
        // Given
        // When
        var message = type.createMessage(name);
        // Then
        assertThat(message).isEqualTo(expectedMessage);
    }

    @ParameterizedTest
    @MethodSource
    void canBeChangedFor_should_tell_us_if_type_are_compatible(GreetingType current, GreetingType newOne, boolean changeable) {
        assertThat(current.canBeChangedFor(newOne)).isEqualTo(changeable);
    }
}