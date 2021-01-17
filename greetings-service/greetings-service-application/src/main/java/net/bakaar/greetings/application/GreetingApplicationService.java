package net.bakaar.greetings.application;

import lombok.RequiredArgsConstructor;
import net.bakaar.greetings.application.exception.GreetingNotFoundException;
import net.bakaar.greetings.domain.CreateGreetingCommand;
import net.bakaar.greetings.domain.Greeting;
import net.bakaar.greetings.domain.GreetingRepository;
import net.bakaar.greetings.domain.UpdateGreetingCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class GreetingApplicationService {
    private final GreetingRepository repository;

    @Transactional
    public Greeting createGreeting(CreateGreetingCommand command) {
        return repository.put(Greeting.of(command.type()).to(command.name()).build());
    }

    @Transactional
    public Greeting changeType(UpdateGreetingCommand command) {
        Greeting greeting = repository.find(command.identifier())
                .orElseThrow(() ->
                        new GreetingNotFoundException(format("Greeting with identifier %s not found", command.identifier()))
                );
        greeting.updateTypeFor(command.newType());
        return repository.put(greeting);
    }
}
