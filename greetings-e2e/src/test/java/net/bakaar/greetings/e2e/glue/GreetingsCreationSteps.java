package net.bakaar.greetings.e2e.glue;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static net.bakaar.greetings.e2e.CucumberLauncherTest.environment;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class GreetingsCreationSteps {

    private final RequestSpecification request = given().log().all(true).contentType("application/json")
            .filters(new ResponseLoggingFilter()).accept("application/json");

    private Response response;

    @When("I create a(n) {word} greeting for {word}")
    public void iCreateAGreetingForName(String type, String name) {
        String url = String.format("http://localhost:%d/rest/api/v1/greetings",
                environment.getServicePort("greetings", 8080));
        response = request.body("""
                {
                   "type": "%s",
                   "name": "%s"
                }""".formatted(type, name)).post(url);
    }

    @Then("I get the message {string}")
    public void iGetTheMessage(String message) {
        response.then().body("message", equalTo(message));
    }
}
