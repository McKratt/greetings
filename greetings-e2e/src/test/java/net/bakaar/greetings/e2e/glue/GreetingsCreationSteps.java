package net.bakaar.greetings.e2e.glue;

import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Testcontainers
public class GreetingsCreationSteps {

    @Container
    private static final PostgreSQLContainer dbContainer = new PostgreSQLContainer("postgres")
            .withUsername("foo")
            .withPassword("secret");

    @Container
    private static final GenericContainer serviceContainer = new GenericContainer("greeting-service")
            .withEnv("spring.datasource.username", dbContainer.getUsername())
            .withEnv("spring.datasource.password", dbContainer.getPassword());
    private final RequestSpecification request = given()
            .baseUri("http://localhost:9080")
            .basePath("/greetings");
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
                          type: %s,
                          name: %s
                        }""".formatted(type, name))
                .post();
    }

    @Then("^I get the message \"([^\"]*)\"$")
    public void iGetTheMessage(String message) {
        String returnedMessage = response.body().jsonPath().getString("$.message");
        assertThat(returnedMessage).isEqualTo(message);
    }

    @Then("^I get an error$")
    public void iGetAnError() {
        assertThat(response.statusCode()).isEqualTo(400);
    }
}
