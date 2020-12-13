package net.bakaar.greetings.rest.glue;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import net.bakaar.greetings.domain.CreateGreetingCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;

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

    @LocalServerPort
    private int port;
    private ResponseEntity<String> response;


    @When("I create a(n) {word} greeting for {word}")
    public void iCreateAGreetingForName(String type, String name) {
        CreateGreetingCommand command = new CreateGreetingCommand(type, name);
        RequestEntity<CreateGreetingCommand> request = RequestEntity
                .post(URI.create(format("http://localhost:%s/rest/api/v1/greetings", port)))
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(command);
        response = restTemplate.exchange(request, String.class);
    }

    @Then("I get the message {string}")
    public void iGetTheMessage(String message) {
        assertThat(response.getStatusCode()).isSameAs(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull().contains(message);
    }

    @Then("I get an error")
    public void iGetAnError() {
        assertThat(response.getStatusCode()).isSameAs(HttpStatus.BAD_REQUEST);
    }
}
