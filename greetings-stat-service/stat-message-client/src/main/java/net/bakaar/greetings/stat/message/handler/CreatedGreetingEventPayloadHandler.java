package net.bakaar.greetings.stat.message.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.bakaar.greetings.stat.application.StatApplicationService;
import net.bakaar.greetings.stat.domain.GreetingCreated;
import net.bakaar.greetings.stat.message.exception.JsonDeserializationException;

import java.net.URI;

@RequiredArgsConstructor
public class CreatedGreetingEventPayloadHandler implements GreetingMessagePayloadHandler {

    private final StatApplicationService service;

    private final ObjectMapper jsonMapper;

    @Override
    public boolean canHandle(URI type) {
        return GreetingCreated.TYPE.equals(type);
    }

    @Override
    public void handle(String payload) {
        GreetingCreated event = null;
        try {
            event = jsonMapper.readValue(payload, GreetingCreated.class);
        } catch (JsonProcessingException e) {
            throw new JsonDeserializationException(e);
        }
        service.handle(event).block();
    }
}
