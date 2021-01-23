package net.bakaar.greetings.e2e.glue;

import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static net.bakaar.greetings.e2e.CucumberLauncherTest.dbContainer;
import static net.bakaar.greetings.e2e.CucumberLauncherTest.serviceContainer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;


public class GreetingsCreationSteps {
    private final RequestSpecification request = given()
            .baseUri("http://localhost:9080")
            .basePath("/rest/api/v1/greetings");
    private Response response;

    @Before
    void setUp() {
        await().until(dbContainer::isHealthy);
        serviceContainer.addEnv("spring.datasource.url", dbContainer.getJdbcUrl());
    }

    @When("I create a(n) {word} greeting for {word}")
    public void iCreateAGreetingForName(String type, String name) {
        response = request
                .body("""
                        {
                           "type": "%s",
                           "name": "%s"
                        }""".formatted(type, name))
                .post();
    }

    @Then("I get the message {string}")
    public void iGetTheMessage(String message) {
        String returnedMessage = response.body().jsonPath().getString("$.message");
        assertThat(returnedMessage).isEqualTo(message);
    }

    @Then("I get an error")
    public void iGetAnError() {
        assertThat(response.statusCode()).isEqualTo(400);
    }
}
