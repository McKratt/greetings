package net.bakaar.greetings.rest.glue;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
public class GreetingsCreationSteps {

    @Autowired
    private MockMvc mockMvc;
    private ResultActions response;

    @When("I create a(n) {word} greeting for {word}")
    public void iCreateAGreetingForName(String type, String name) throws Exception {
        response = mockMvc.perform(
                post("/rest/api/v1/greetings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "type": "%s",
                                  "name": "%s"
                                }""".formatted(type, name))
        );
    }

    @Then("I get the message {string}")
    public void iGetTheMessage(String message) throws Exception {
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Then("I get an error")
    public void iGetAnError() throws Exception {
        response.andExpect(status().is4xxClientError())
                .andExpect(status().is(400));
    }
}
