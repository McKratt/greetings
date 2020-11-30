package net.bakaar.greetings.rest;

import net.bakaar.greetings.domain.Greeting;
import net.bakaar.greetings.rest.model.GreetingMessage;
import org.springframework.stereotype.Component;

@Component
public class GreetingToMessageMapper {
    public GreetingMessage mapToMessage(Greeting greeting) {
        return new GreetingMessage(greeting.getMessage());
    }
}
