package net.bakaar.greetings.stat.application.glue;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.bakaar.greetings.stat.application.GreetingsRepository;
import net.bakaar.greetings.stat.application.StatApplicationService;
import net.bakaar.greetings.stat.application.readmodel.Greeting;
import net.bakaar.greetings.stat.domain.GreetingCreated;
import net.bakaar.greetings.stat.domain.GreetingsStats;
import net.bakaar.greetings.stat.domain.StatRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class GreetingsStatsApplicationSteps {

    private final UUID identifier = UUID.randomUUID();

    private final StatRepository statRepository = mock(StatRepository.class);
    private final GreetingsRepository greetingsRepository = mock(GreetingsRepository.class);
    private final StatApplicationService service = new StatApplicationService(statRepository, greetingsRepository);
    private GreetingsStats stats = new GreetingsStats(new HashMap<>(Map.of("BIRTHDAY", 0L, "ANNIVERSARY", 0L, "CHRISTMAS", 0L)));
    private String type = "ANNIVERSARY";

    @Given("the christmas greetings counter is equal to {long}")
    public void the_christmas_greetings_counter_is_equal_to(long value) {
        stats.getCounters().replace("CHRISTMAS", value);
    }

    @When("I create a greeting")
    public void i_create_a_greetings() {
        i_create_a_greeting("");
    }

    @When("I create a {word} greeting")
    public void i_create_a_greeting(String input) {
        if (input != null && !input.trim().isBlank()) {
            this.type = input.toUpperCase(Locale.ROOT);
        }
        // receive event
        var event = mock(GreetingCreated.class);
        given(event.identifier()).willReturn(identifier);
        // get the stat object from DB
        given(statRepository.pop()).willReturn(CompletableFuture.completedFuture(stats));
        // call the greetings service to know the type
        var greeting = new Greeting(this.type, "Copernicus");
        given(greetingsRepository.getGreetingForIdentifier(identifier)).willReturn(Mono.just(greeting));
        // update the stat object
        StepVerifier.create(service.handle(event))
                .verifyComplete();
    }

    @Then("the counter should be {long}")
    public void the_counter_should_be(Long counter) {
        StepVerifier.create(service.retrieveGreetingsStats())
                .assertNext(found -> assertThat(found.getStatsFor(type)).contains(counter))
                .verifyComplete();

    }
}
