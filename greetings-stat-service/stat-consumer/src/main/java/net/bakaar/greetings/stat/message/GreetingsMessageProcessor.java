package net.bakaar.greetings.stat.message;

import lombok.extern.slf4j.Slf4j;
import net.bakaar.greetings.message.GreetingsMessage;
import net.bakaar.greetings.stat.message.exception.HandlerNotFoundException;
import net.bakaar.greetings.stat.message.handler.GreetingMessagePayloadHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class GreetingsMessageProcessor {

    private final Set<GreetingMessagePayloadHandler> handlers = new HashSet<>();

    @Autowired
    public void setHandlers(Set<GreetingMessagePayloadHandler> handlers) {
        this.handlers.addAll(handlers);
    }

    @KafkaListener(topics = "${greetings.message.topic}")
    public void processMessage(GreetingsMessage message, Acknowledgment ack) {
        handlers.stream()
                .filter(handler -> handler.canHandle(message.type()))
                .findFirst()
                .orElseThrow(() -> new HandlerNotFoundException(message.type()))
                .handle(message.payload())
                .onErrorMap(exception -> exception) // makes the exception go out to block the Process.
                .subscribe(null,
                        null,
                        ack::acknowledge);
    }
}
