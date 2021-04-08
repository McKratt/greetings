package net.bakaar.greetings.rest;

import net.bakaar.greetings.domain.Greeting;
import org.springframework.stereotype.Component;

@Component
public class GreetingMapper {
    public GreetingMessage mapToMessage(Greeting greeting) {
        return new GreetingMessage(greeting.getMessage());
    }

    public GreetingJson mapToJson(Greeting greeting) {
        return new GreetingJson(greeting.getType().name(), greeting.getName());
    }
}
