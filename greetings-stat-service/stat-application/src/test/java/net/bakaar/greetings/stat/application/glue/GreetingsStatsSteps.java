package net.bakaar.greetings.stat.application.glue;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.bakaar.greetings.stat.application.GreetingsRepository;
import net.bakaar.greetings.stat.application.StatApplicationService;
import net.bakaar.greetings.stat.application.readmodel.Greeting;
import net.bakaar.greetings.stat.domain.GreetingCreated;
import net.bakaar.greetings.stat.domain.GreetingType;
import net.bakaar.greetings.stat.domain.GreetingsStats;
import net.bakaar.greetings.stat.domain.StatRepository;
import org.mockito.ArgumentCaptor;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static net.bakaar.greetings.stat.domain.GreetingType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GreetingsStatsSteps {

    private final UUID identifier = UUID.randomUUID();

    private final StatRepository statRepository = mock(StatRepository.class);
    private final GreetingsRepository greetingsRepository = mock(GreetingsRepository.class);
    private final StatApplicationService service = new StatApplicationService(statRepository, greetingsRepository);
    private final GreetingsStats stats = new GreetingsStats(new HashMap<>(Map.of(BIRTHDAY, 0L, ANNIVERSARY, 0L, CHRISTMAS, 0L)));
    private final GreetingType type = GreetingType.ANNIVERSARY;

    @When("I create a greeting")
    public void i_create_a_greetings() {
        // receive event
        var event = mock(GreetingCreated.class);
        given(event.identifier()).willReturn(identifier);
        // get the stat object from DB
        given(statRepository.pop()).willReturn(CompletableFuture.completedFuture(stats));
        given(statRepository.put(any())).willReturn(CompletableFuture.completedFuture(null));
        // call the greetings service to know the type
        var greeting = new Greeting(type, "Copernicus");
        given(greetingsRepository.getGreetingForIdentifier(identifier)).willReturn(Mono.just(greeting));
        // update the stat object
        service.handle(event).subscribe();
    }

    @Then("the counter should be {long}")
    public void the_counter_should_be(Long counter) {
        // check the saved stat object contains one of the counter to counter value
        var captor = ArgumentCaptor.forClass(GreetingsStats.class);
        verify(statRepository).put(captor.capture());
        var stats = captor.getValue();
        assertThat(stats).isNotNull();
        assertThat(stats.getStatsFor(type)).isPresent().get().isEqualTo(counter);
    }
}
