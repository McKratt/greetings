package net.bakaar.greetings.domain;


import lombok.Getter;
import net.bakaar.greetings.domain.exception.GreetingMissingNameException;
import net.bakaar.greetings.domain.exception.GreetingMissingTypeException;

import java.util.UUID;

public class Greeting {

    @Getter
    private final GreetingType type;

    @Getter
    private final UUID identifier;

    private final String name;

    public Greeting(String type, String name) {
        this.identifier = UUID.randomUUID();
        this.name = name;
        this.type = GreetingType.valueOf(type.toUpperCase());
    }

    public static TypedBuilder of(String type) {
        return new Builder(type);
    }

    public interface TypedBuilder {

        NamedBuilder to(String name);
    }

    public interface NamedBuilder {

        Greeting build();
    }

    public static class Builder implements TypedBuilder, NamedBuilder {

        private final String type;

        private String name;

        public Builder(String type) {
            if (type == null) {
                throw new GreetingMissingTypeException();
            }
            this.type = type;
        }

        @Override
        public NamedBuilder to(String name) {
            if (name == null) {
                throw new GreetingMissingNameException();
            }
            this.name = name;
            return this;
        }

        @Override
        public Greeting build() {
            return new Greeting(type, name);
        }
    }

    public String getMessage() {
        return null;
    }

}
