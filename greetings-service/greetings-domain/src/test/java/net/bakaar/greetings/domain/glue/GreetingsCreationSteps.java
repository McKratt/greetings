package net.bakaar.greetings.domain.glue;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.bakaar.greetings.domain.Greeting;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;


public class GreetingsCreationSteps {

    private Greeting createdGreeting;
    private Throwable thrown;


    @Given("an existing {word} greeting")
    public void an_existing_greeting(String type) {
        createdGreeting = Greeting.of(type).to("toto").build();
    }

    @When("I create a(n) {word} greeting for {word}")
    public void iCreateAGreetingForName(String type, String name) {
        thrown = catchThrowable(() -> createdGreeting = Greeting.of(type).to(name).build());
    }

    @When("I change the type to {word}")
    public void i_change_the_type_to(String type) {
        thrown = catchThrowable(() -> createdGreeting.updateTypeFor(type));
    }

    @Then("I get the message {string}")
    public void iGetTheMessage(String message) {
        assertThat(createdGreeting.getMessage()).isEqualTo(message);
    }

    @Then("a Greeting is created")
    public void a_greeting_is_created() {
        assertThat(createdGreeting).isNotNull();
    }

    @Then("I get an error")
    public void iGetAnError() {
        assertThat(thrown).isNotNull();
    }

    @Then("the greeting is now a {word} one")
    public void the_greeting_is_now_a_new_type_one(String type) {
        assertThat(createdGreeting.getType()).hasToString(type.toUpperCase(Locale.ROOT));
    }
}
