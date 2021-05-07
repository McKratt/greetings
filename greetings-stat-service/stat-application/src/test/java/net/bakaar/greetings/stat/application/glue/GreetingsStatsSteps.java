package net.bakaar.greetings.stat.application.glue;

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
import java.util.Map;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class GreetingsStatsSteps {

    private final UUID identifier = UUID.randomUUID();

    private final StatRepository statRepository = mock(StatRepository.class);
    private final GreetingsRepository greetingsRepository = mock(GreetingsRepository.class);
    private final StatApplicationService service = new StatApplicationService(statRepository, greetingsRepository);
    private final GreetingsStats stats = new GreetingsStats(new HashMap<>(Map.of("BIRTHDAY", 0L, "ANNIVERSARY", 0L, "CHRISTMAS", 0L)));
    private final String type = "ANNIVERSARY";

    @When("I create a greeting")
    public void i_create_a_greetings() {
        // receive event
        var event = mock(GreetingCreated.class);
        given(event.identifier()).willReturn(identifier);
        // get the stat object from DB
        given(statRepository.pop()).willReturn(stats);
        // call the greetings service to know the type
        var greeting = new Greeting(type, "Copernicus");
        given(greetingsRepository.getGreetingForIdentifier(identifier)).willReturn(Mono.just(greeting));
        // update the stat object
        StepVerifier.create(service.handle(event))
                .verifyComplete();
    }

    @Then("the counter should be {long}")
    public void the_counter_should_be(Long counter) {
        StepVerifier.create(service.retrieveGreetingsStats())
                .expectNextMatches(returned -> returned.getStatsFor(type).map(counter::equals).get())
                .verifyComplete();

    }
}
