package net.bakaar.greetings.stat.rest.glue;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.bakaar.greetings.stat.application.GreetingsRepository;
import net.bakaar.greetings.stat.application.StatApplicationService;
import net.bakaar.greetings.stat.application.readmodel.Greeting;
import net.bakaar.greetings.stat.domain.GreetingCreated;
import net.bakaar.greetings.stat.domain.GreetingsStats;
import net.bakaar.greetings.stat.domain.StatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.mockito.BDDMockito.given;


public class StatsRestSteps {

    private String type = "CHRISTMAS";
    @Autowired
    private WebTestClient testClient;

    @Autowired
    private StatRepository statRepository;

    @Autowired
    private StatApplicationService service;

    @Autowired
    private GreetingsRepository repository;


    @Given("the christmas greetings counter is equal to {long}")
    public void the_christmas_greetings_counter_is_equal_to(long counter) {
        given(statRepository.pop()).willReturn(CompletableFuture.completedFuture(new GreetingsStats(new HashMap<>(Map.of("CHRISTMAS", counter)))));
    }

    @When("I create a {word} greeting")
    public void i_create_a_greeting(String inputType) {
        if (inputType != null && !inputType.trim().isBlank()) {
            this.type = inputType;
        }
        var identifier = UUID.randomUUID();
        var greeting = new Greeting(type, "Lucius");
        given(repository.getGreetingForIdentifier(identifier)).willReturn(Mono.just(greeting));
        var event = new GreetingCreated(identifier);
        StepVerifier.create(service.handle(event))
                .verifyComplete();
    }


    @When("I create a greeting")
    public void i_create_a_greetings() {
        given(statRepository.pop()).willReturn(CompletableFuture.completedFuture(new GreetingsStats(new HashMap<>())));
        i_create_a_greeting("");
    }

    @Then("the counter should be {int}")
    public void the_counter_should_be(Integer counter) {
        testClient.get().uri("rest/api/v1/stats")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody()
                .json("""
                        {
                            "counters" : {
                                "%s": %d
                            }
                        }""".formatted(type.toUpperCase(Locale.ROOT), counter));
    }
}
