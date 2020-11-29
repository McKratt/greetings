package net.bakaar.greetings.application;

import net.bakaar.grettings.domain.CreateGreetingCommand;
import net.bakaar.grettings.domain.Greeting;
import org.springframework.stereotype.Service;

@Service
public class GreetingApplicationService {
    public Greeting createGreeting(CreateGreetingCommand command) {
        return null;
    }
}
