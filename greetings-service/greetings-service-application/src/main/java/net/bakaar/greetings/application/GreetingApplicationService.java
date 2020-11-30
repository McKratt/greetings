package net.bakaar.greetings.application;

import lombok.RequiredArgsConstructor;
import net.bakaar.greetings.domain.CreateGreetingCommand;
import net.bakaar.greetings.domain.Greeting;
import net.bakaar.greetings.domain.GreetingRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GreetingApplicationService {

    private final GreetingRepository repository;

    public Greeting createGreeting(CreateGreetingCommand command) {
        return repository.put(Greeting.of(command.type()).to(command.name()).build());
    }
}
