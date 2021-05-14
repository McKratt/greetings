package net.bakaar.greetings.application.glue;

import lombok.Getter;
import net.bakaar.greetings.domain.event.EventEmitter;
import net.bakaar.greetings.domain.event.GreetingsEvent;
import org.springframework.stereotype.Component;

@Getter
@Component
public class StubbedEventEmitter implements EventEmitter {

    private GreetingsEvent emittedEvent;

    @Override
    public void emit(GreetingsEvent event) {
        this.emittedEvent = event;
    }
}
