package net.bakaar.greetings.domain.glue;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.bakaar.greetings.domain.Greeting;
import net.bakaar.greetings.domain.exception.GreetingWrongTypeException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;


public class GreetingsCreationSteps {

    private Greeting greeting;
    private Throwable thrown;

    @When("I create a(n) {word} greeting for {word}")
    public void iCreateAGreetingForName(String type, String name) {
        thrown = catchThrowable(() -> greeting = Greeting.of(type).to(name).build());
    }

    @Then("I get the message {string}")
    public void iGetTheMessage(String message) {
        assertThat(greeting.getMessage()).isEqualTo(message);
    }

    @Then("I get an error")
    public void iGetAnError() {
        assertThat(thrown).isNotNull().isInstanceOf(GreetingWrongTypeException.class);
    }
}
