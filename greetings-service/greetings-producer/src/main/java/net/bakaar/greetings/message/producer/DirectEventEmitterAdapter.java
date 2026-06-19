package net.bakaar.greetings.message.producer;

import lombok.RequiredArgsConstructor;
import net.bakaar.greetings.domain.event.EventEmitter;
import net.bakaar.greetings.domain.event.GreetingsEvent;
import net.bakaar.greetings.message.GreetingsMessage;
import org.springframework.kafka.core.KafkaTemplate;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;

@RequiredArgsConstructor
public class DirectEventEmitterAdapter implements EventEmitter {
    private final GreetingsProducerProperties properties;
    private final ObjectMapper mapper;
    private final KafkaTemplate<String, GreetingsMessage> template;

    @Override
    public void emit(GreetingsEvent event) {
        try {
            template.send(properties.getTopicName(),
                    new GreetingsMessage(
                            URI.create("https://bakaar.net/greetings/events/greeting-created"),
                            mapper.writeValueAsString(event))
            );
        } catch (JacksonException e) {
            throw new ProducerException(e);
        }
    }
}
