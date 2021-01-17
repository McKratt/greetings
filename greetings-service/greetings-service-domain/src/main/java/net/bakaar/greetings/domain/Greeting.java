package net.bakaar.greetings.domain;


import lombok.Getter;
import net.bakaar.greetings.domain.exception.GreetingMissingNameException;
import net.bakaar.greetings.domain.exception.GreetingMissingTypeException;
import net.bakaar.greetings.domain.exception.GreetingUnmodifiableTypeException;

import java.util.UUID;

@Getter
public class Greeting {

    private final UUID identifier;
    private final String name;
    private GreetingType type;

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

    public String getMessage() {
        return type.createMessage(name);
    }

    public void updateTypeFor(String type) {
        // Check if the new type is correct
        GreetingType newType = GreetingType.of(type);
        // If the current Type is not modifiable so you don't have the right to change
        if (!this.type.canBeChangedFor(newType)) {
            throw new GreetingUnmodifiableTypeException();
        }
        // Change the type in all the other cases
        this.type = newType;
    }

    public interface TypedBuilder {

        Builder to(String name);
    }

    public static class Builder implements TypedBuilder {

        private final String type;

        private String name;
        private String identifier;

        private Builder(String type) {
            if (type == null) {
                throw new GreetingMissingTypeException();
            }
            this.type = type;
        }

        public Builder withIdentifier(String identifier) {
            this.identifier = identifier;
            return this;
        }

        public Builder to(String name) {
            if (name == null) {
                throw new GreetingMissingNameException();
            }
            this.name = name;
            return this;
        }

        public Greeting build() {
            if (identifier == null || "".equals(identifier.trim())) {
                return new Greeting(type, name);
            } else {
                return new Greeting(type, name, identifier);
            }
        }
    }
}
