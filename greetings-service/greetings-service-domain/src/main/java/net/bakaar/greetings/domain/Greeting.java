package net.bakaar.greetings.domain;


import lombok.Getter;
import net.bakaar.greetings.domain.exception.GreetingMissingNameException;
import net.bakaar.greetings.domain.exception.GreetingMissingTypeException;

import java.util.UUID;

@Getter
public class Greeting {

    private final GreetingType type;

    private final UUID identifier;

    private final String name;

    private Greeting(String type, String name) {
        this.identifier = UUID.randomUUID();
        this.name = name;
        this.type = GreetingType.of(type);
    }

    private Greeting(String type, String name, String identifier) {
        this.identifier = UUID.fromString(identifier);
        this.name = name;
        this.type = GreetingType.of(type);
    }

    public static TypedBuilder of(String type) {
        return new Builder(type);
    }

    public interface TypedBuilder {

        NamedBuilder to(String name);
    }

    public interface NamedBuilder {

        Greeting build();

        NamedBuilder withIdentifier(String identifier);
    }

    private static class Builder implements TypedBuilder, NamedBuilder {

        private final String type;

        private String name;
        private String identifier;

        private Builder(String type) {
            if (type == null) {
                throw new GreetingMissingTypeException();
            }
            this.type = type;
        }

        @Override
        public NamedBuilder withIdentifier(String identifier) {
            this.identifier = identifier;
            return this;
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
            if (identifier == null || identifier.isBlank()) {
                return new Greeting(type, name);
            } else {
                return new Greeting(type, name, identifier);
            }
        }
    }

    public String getMessage() {
        return type.createMessage(name);
    }

}
