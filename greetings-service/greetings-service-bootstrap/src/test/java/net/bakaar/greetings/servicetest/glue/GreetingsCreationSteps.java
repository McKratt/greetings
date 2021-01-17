package net.bakaar.greetings.servicetest.glue;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import net.bakaar.greetings.persist.GreetingJpaEntity;
import net.bakaar.greetings.persist.GreetingJpaRepository;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.LocalDateTime;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static net.bakaar.greetings.servicetest.CucumberLauncherIT.dbContainer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class GreetingsCreationSteps {


    private final String identifier = UUID.randomUUID().toString();
    private final RequestSpecification request = given()
            .log().all(true)
            .contentType("application/json")
            .accept("application/json");
    private Response response;
    @LocalServerPort
    private int port;
    @Autowired
    private GreetingJpaRepository japRepository;

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",
                () -> String.format("jdbc:postgresql://localhost:%d/%s",
                        dbContainer.getFirstMappedPort(), dbContainer.getDatabaseName()));
        registry.add("spring.datasource.username", dbContainer::getUsername);
        registry.add("spring.datasource.password", dbContainer::getPassword);
    }

    @Given("an existing {word} greeting")
    public void an_existing_greeting(String type) {
        GreetingJpaEntity entity = new GreetingJpaEntity();
        entity.setType(type);
        entity.setIdentifier(identifier);
        entity.setName("Koala");
        entity.setCreatedAt(LocalDateTime.now());
        japRepository.save(entity);
    }

    @When("I create a(n) {word} greeting for {word}")
    public void iCreateAGreetingForName(String type, String name) {
        log.info("port = " + port);
        response = request
                .body("""
                        {
                          "type": "%s",
                          "name": "%s"
                        }""".formatted(type, name))
                .contentType("application/json")
                .post(format("http://localhost:%d/rest/api/v1/greetings", port));
    }

    @When("I change the type to {word}")
    public void i_change_the_type_to(String type) {
        response = request
                .body(format("{\"newType\":\"%s\"}", type))
                .put(format("http://localhost:%d/rest/api/v1/greetings/%s", port, identifier));
    }

    @Then("I get the message {string}")
    public void iGetTheMessage(String message) {
        response.then().body("message", equalTo(message));
    }

    @Then("I get an error")
    public void iGetAnError() {
        assertThat(response.statusCode()).isEqualTo(400);
    }

    @Then("the greeting is now a {word} one")
    public void the_greeting_is_now_a_new_type_one(String type) {
        response.then().log().everything(true).body("message", Matchers.containsStringIgnoringCase(type));
    }
}
