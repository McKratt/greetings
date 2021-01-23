package net.bakaar.greetings.rest;

import net.bakaar.greetings.application.GreetingApplicationService;
import net.bakaar.greetings.domain.Greeting;
import net.bakaar.greetings.domain.UpdateGreetingCommand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static java.lang.String.format;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This test is meant to check if we put the right annotation upon our endpoints. Nothing else...
 */
@WebMvcTest(controllers = GreetingsController.class)
@AutoConfigureMockMvc
@Import({GreetingToMessageMapper.class})
class GreetingsControllerIT {

    private final String basePath = "/rest/api/v1/greetings";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GreetingApplicationService service;

    @Test
    void createGreeting_should_respond_with_correct_location_and_content_type() throws Exception {
        // Given
        var greeting = mock(Greeting.class);
        var identifier = UUID.randomUUID();
        given(greeting.getIdentifier()).willReturn(identifier);

        given(service.createGreeting(any())).willReturn(greeting);
        // When
        var response = mockMvc.perform(
                post(basePath)
                        .accept(APPLICATION_JSON, APPLICATION_PROBLEM_JSON)
                        .content("""
                                {
                                  "type": "anniversary",
                                  "name": "Edouard"
                                }""")
                        .contentType(APPLICATION_JSON)
        );
        // Then
        response.andExpect(
                status().isCreated()
        ).andExpect(
                header().string("location", format("http://localhost/rest/api/v1/greetings/%s", identifier.toString()))
        ).andExpect(
                header().string(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON.toString())
        );
    }

    @Test
    void updateGreeting_should_answer_with_correct_content_type() throws Exception {
        // Given
        var greeting = mock(Greeting.class);
        var identifier = UUID.randomUUID();
        var type = "anniversary";
        var command = new UpdateGreetingCommand(identifier, type);
        given(service.changeType(command)).willReturn(greeting);
        // When
        var response = mockMvc.perform(
                put(basePath + "/" + identifier.toString())
                        .accept(APPLICATION_JSON, APPLICATION_PROBLEM_JSON)
                        .content("""
                                {
                                  "newType":"%s"
                                }
                                """.formatted(type))
                        .contentType(APPLICATION_JSON)
        );
        // Then
        response.andExpect(
                status().isOk()
        ).andExpect(
                header().string(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON.toString())
        );
    }
}
