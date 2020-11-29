package net.bakaar.greetings.application.glue;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import net.bakaar.greetings.application.GreetingApplicationService;
import net.bakaar.grettings.domain.CreateGreetingCommand;
import net.bakaar.grettings.domain.Greeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@CucumberContextConfiguration
@ContextConfiguration(classes = GreetingApplicationTestConfiguration.class)
public class GreetingsCreationSteps {

    @Autowired
    private GreetingApplicationService service;

    private Greeting createdGreeting;

    private Throwable thrown;

    @When("I create a(n) {word} greeting for {word}")
    public void iCreateAGreetingForName(String type, String name) {
        thrown = catchThrowable(() -> createdGreeting = service.createGreeting(new CreateGreetingCommand(type, name)));
    }

    @Then("I get the message {string}")
    public void iGetTheMessage(String message) {
        assertThat(createdGreeting.getMessage()).isEqualTo(message);
    }

    @Then("I get an error")
    public void iGetAnError() {
        assertThat(thrown).isNotNull().isInstanceOf(IllegalArgumentException.class);
    }
}
