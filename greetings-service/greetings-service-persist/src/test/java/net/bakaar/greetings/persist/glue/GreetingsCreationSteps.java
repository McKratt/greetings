package net.bakaar.greetings.persist.glue;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import net.bakaar.greetings.domain.Greeting;
import net.bakaar.greetings.domain.GreetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

/**
 * Not really sure this test brings something...
 * This layer will be tested with the BDD test in bootstrap.
 */
@CucumberContextConfiguration
@DataJpaTest
@ComponentScan(basePackages = "net.bakaar.greetings")
public class GreetingsCreationSteps {


    @Autowired
    private GreetingRepository repository;

    private Greeting greeting;

    private Throwable thrown;

    @When("I create a(n) {word} greeting for {word}")
    public void iCreateAGreetingForName(String type, String name) {
        thrown = catchThrowable(() -> greeting = repository.put(Greeting.of(type).to(name).build()));
    }

    @Then("I get the message {string}")
    public void iGetTheMessage(String message) {
        assertThat(greeting.getMessage()).isEqualTo(message);
    }

    @Then("I get an error")
    public void iGetAnError() {
        assertThat(thrown).isNotNull().isInstanceOf(IllegalArgumentException.class);
    }
}
