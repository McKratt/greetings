package net.bakaar.greetings.servicetest.glue;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Testcontainers
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class GreetingsCreationSteps {

    @Container
    private static final PostgreSQLContainer dbContainer = new PostgreSQLContainer("postgres")
            .withUsername("foo")
            .withPassword("secret");

    private final RequestSpecification request = given()
            .basePath("/rest/api/v1/greetings");
    private Response response;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        request.baseUri(format("http://localhost:%d", port));
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
