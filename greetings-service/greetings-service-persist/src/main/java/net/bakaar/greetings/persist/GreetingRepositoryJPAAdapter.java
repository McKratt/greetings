package net.bakaar.greetings.persist;

import net.bakaar.greetings.domain.Greeting;
import net.bakaar.greetings.domain.GreetingRepository;
import org.springframework.stereotype.Component;

@Component
public class GreetingRepositoryJPAAdapter implements GreetingRepository {
    @Override
    public Greeting put(Greeting greeting) {
        return null;
    }
}
