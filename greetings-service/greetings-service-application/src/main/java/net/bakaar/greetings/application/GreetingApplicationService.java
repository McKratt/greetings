package net.bakaar.greetings.application;

import lombok.RequiredArgsConstructor;
import net.bakaar.greetings.application.exception.GreetingNotFoundException;
import net.bakaar.greetings.domain.CreateGreetingCommand;
import net.bakaar.greetings.domain.Greeting;
import net.bakaar.greetings.domain.GreetingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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
    public Greeting changeType(UUID identifier, String newType) {
        Greeting greeting = repository.find(identifier)
                .orElseThrow(() ->
                        new GreetingNotFoundException(format("Greeting with identifier %s not found", identifier.toString()))
                );
        greeting.updateTypeFor(newType);
        return repository.put(greeting);
    }
}
