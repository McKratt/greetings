package net.bakaar.greetings.application;

import lombok.RequiredArgsConstructor;
import net.bakaar.greetings.application.exception.GreetingNotFoundException;
import net.bakaar.greetings.domain.CreateGreetingCommand;
import net.bakaar.greetings.domain.Greeting;
import net.bakaar.greetings.domain.GreetingRepository;
import net.bakaar.greetings.domain.UpdateGreetingCommand;
import net.bakaar.greetings.domain.event.EventEmitter;
import net.bakaar.greetings.domain.event.GreetingCreated;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GreetingApplicationService {
    private final GreetingRepository repository;
    private final EventEmitter emitter;

    @Transactional
    public Greeting createGreeting(CreateGreetingCommand command) {
        var greeting = Greeting.of(command.type()).to(command.name()).build();
        emitter.emit(GreetingCreated.of(greeting));
        return repository.put(greeting);
    }

    @Transactional
    public Greeting changeType(UpdateGreetingCommand command) {
        Greeting greeting = read(command.identifier());
        greeting.updateTypeFor(command.newType());
        return repository.put(greeting);
    }

    @Transactional(readOnly = true)
    public Greeting read(UUID identifier) {
        return repository.find(identifier)
                .orElseThrow(() ->
                        new GreetingNotFoundException("Greeting with identifier %s not found".formatted(identifier))
                );
    }
}
