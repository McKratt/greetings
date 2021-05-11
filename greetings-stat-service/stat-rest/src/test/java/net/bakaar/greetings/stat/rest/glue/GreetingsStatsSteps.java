package net.bakaar.greetings.stat.rest.glue;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import net.bakaar.greetings.stat.application.StatApplicationService;
import net.bakaar.greetings.stat.application.readmodel.Greeting;
import net.bakaar.greetings.stat.domain.GreetingCreated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebClient
public class GreetingsStatsSteps {

    private final String type = "CHRISTMAS";
    @Autowired
    private WebTestClient testClient;

    @Autowired
    private StatApplicationService service;

    @Autowired
    private TestGreetingsRepository repository;


    @When("I create a greeting")
    public void i_create_a_greetings() {
        var identifier = UUID.randomUUID();
        var greeting = new Greeting(type, "Lucius");
        repository.setGreeting(greeting, identifier);
        var event = new GreetingCreated(identifier);
        StepVerifier.create(service.handle(event))
                .verifyComplete();
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
                        }""".formatted(type, counter));
//                .jsonPath("counters.%s", type).isEqualTo(counter);
    }
}
