package net.bakaar.greetings.domain;


import lombok.RequiredArgsConstructor;
import net.bakaar.greetings.domain.exception.GreetingWrongTypeException;

import static java.lang.String.format;

@RequiredArgsConstructor
public enum GreetingType {
    BIRTHDAY(name -> format("Happy Birthday %s !", name)),
    ANNIVERSARY(name -> format("Joyful Anniversary %s !", name)),
    CHRISTMAS(name -> format("Merry Christmas %s !", name));

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
