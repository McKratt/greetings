package net.bakaar.grettings.domain;


public class Greeting {
    private GreetingType type;

    public static TypedBuilder of(String type) {
        return null;
    }

    public interface TypedBuilder {

        NamedBuilder to(String name);
    }

    public interface NamedBuilder {

        Greeting build();
    }


    public String getMessage() {
        return null;
    }

    public GreetingType getType() {
        return type;
    }
}
