package net.bakaar.greetings.stat.domain;

import net.bakaar.greetings.stat.domain.exception.GreetingWrongTypeException;

public enum GreetingType {
    BIRTHDAY,
    ANNIVERSARY,
    CHRISTMAS;

    public static GreetingType of(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new GreetingWrongTypeException(name, e);
        }
    }
}
