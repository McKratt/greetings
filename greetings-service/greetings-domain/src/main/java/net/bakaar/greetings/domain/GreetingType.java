package net.bakaar.greetings.domain;


import lombok.RequiredArgsConstructor;
import net.bakaar.greetings.domain.exception.GreetingWrongTypeException;

@RequiredArgsConstructor
public enum GreetingType {
    BIRTHDAY(name -> "Happy Birthday %s !".formatted(name)),
    ANNIVERSARY(name -> "Joyful Anniversary %s !".formatted(name)),
    CHRISTMAS(name -> "Merry Christmas %s !".formatted(name));

    private final MessageCreator messageCreator;

    public static GreetingType of(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new GreetingWrongTypeException(name, e);
        }
    }

    public String createMessage(String name) {
        return messageCreator.createMessage(name);
    }

    public boolean canBeChangedFor(GreetingType newOne) {
        return this != CHRISTMAS && newOne != CHRISTMAS;
    }
}
