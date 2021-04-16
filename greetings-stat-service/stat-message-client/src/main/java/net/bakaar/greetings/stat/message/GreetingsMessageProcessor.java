package net.bakaar.greetings.stat.message;

import net.bakaar.greetings.stat.message.exception.HandlerNotFoundException;
import net.bakaar.greetings.stat.message.handler.GreetingMessagePayloadHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class GreetingsMessageProcessor {

    @Autowired
    private final Set<GreetingMessagePayloadHandler> handlers = new HashSet<>();

    @KafkaListener(topics = "${greetings.message.topic}")
    public void processMessage(GreetingMessage message, Acknowledgment ack) {
        handlers.stream()
                .filter(handler -> handler.canHandle(message.type()))
                .findFirst()
                .ifPresentOrElse(handler ->
                                handler.handle(message.payload())
                                        .subscribe(null, null, ack::acknowledge),
                        () -> {
                            throw new HandlerNotFoundException(message.type());
                        });
    }
}
