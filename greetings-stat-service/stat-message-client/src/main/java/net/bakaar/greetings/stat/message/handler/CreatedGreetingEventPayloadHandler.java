package net.bakaar.greetings.stat.message.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.bakaar.greetings.stat.application.StatApplicationService;
import net.bakaar.greetings.stat.domain.GreetingCreated;
import net.bakaar.greetings.stat.message.exception.JsonDeserializationException;
import reactor.core.publisher.Mono;

import java.net.URI;

@RequiredArgsConstructor
public class CreatedGreetingEventPayloadHandler implements GreetingMessagePayloadHandler {

    private final StatApplicationService service;

    private final ObjectMapper jsonMapper;

    private final static URI TYPE = URI.create("http://bakaar.net/greetings/events/greeting-created");

    @Override
    public boolean canHandle(URI type) {
        return TYPE.equals(type);
    }

    @Override
    public Mono<Void> handle(String payload) {
        return Mono.fromCallable(() -> jsonMapper.readValue(payload, GreetingCreated.class))
                .onErrorMap(JsonDeserializationException::new)
                .flatMap(service::handle);

    }
}
