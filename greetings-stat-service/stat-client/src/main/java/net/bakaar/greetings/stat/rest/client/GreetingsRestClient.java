package net.bakaar.greetings.stat.rest.client;

import net.bakaar.greetings.stat.application.readmodel.Greeting;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@HttpExchange(url = "/rest/api/v1/greetings")
public interface GreetingsRestClient {

    @GetExchange(url = "/{id}", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<Greeting> findGreetingFromId(@PathVariable UUID id);
}
