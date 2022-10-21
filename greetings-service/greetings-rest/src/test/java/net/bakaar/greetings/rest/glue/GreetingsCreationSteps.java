package net.bakaar.greetings.rest.glue;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import net.bakaar.greetings.domain.CreateGreetingCommand;
import net.bakaar.greetings.domain.GreetingRepository;
import net.bakaar.greetings.rest.UpdateGreetingCommandDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
public class GreetingsCreationSteps {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private GreetingRepository repository;

    @LocalServerPort
    private int port;
    private ResponseEntity<String> response;


    @Given("an existing {word} greeting")
    public void an_existing_greeting(String type) {
        var command = new CreateGreetingCommand(type, "George");
        var request = RequestEntity
                .post(URI.create(format("http://localhost:%s/rest/api/v1/greetings", port)))
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(command);
        response = restTemplate.exchange(request, String.class);
    }

    @When("I create a(n) {word} greeting for {word}")
    public void iCreateAGreetingForName(String type, String name) {
        var command = new CreateGreetingCommand(type, name);
        var request = RequestEntity
                .post(URI.create(format("http://localhost:%s/rest/api/v1/greetings", port)))
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(command);
        response = restTemplate.exchange(request, String.class);
    }

    @When("I change the type to {word}")
    public void i_change_the_type_to(String type) {
        var updateGreetingCommand = new UpdateGreetingCommandDTO();
        updateGreetingCommand.setNewType(type);
        var request = RequestEntity
                .put(URI.create(response.getHeaders().getLocation().getPath()))
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(updateGreetingCommand);
        response = restTemplate.exchange(request, String.class);
    }

    @Then("I get the message {string}")
    public void iGetTheMessage(String message) {
        assertThat(response.getStatusCode()).isSameAs(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull().contains(message);
    }

    @Then("a Greeting is created")
    public void a_greeting_is_created() {
        var identifier = extractIdentifierFromUrl(response.getHeaders().get("location").toString()).get();
        var greeting = repository.find(UUID.fromString(identifier));
        assertThat(greeting).isNotEmpty();
    }

    private Optional<String> extractIdentifierFromUrl(String url) {
        var pattern = Pattern.compile("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})");
        var match = pattern.matcher(url);
        if (match.find()) {
            return Optional.ofNullable(match.group(1));
        }
        return Optional.empty();
    }

    @Then("I get an error")
    public void iGetAnError() {
        // Here pop up a 500 because the error propagation interceptor is only there in the bootstrap project.
        assertThat(response.getStatusCode()).isSameAs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Then("the greeting is now a {word} one")
    public void the_greeting_is_now_a_new_type_one(String type) {
        assertThat(response.getStatusCode()).isSameAs(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().containsIgnoringCase(type);
    }
}
