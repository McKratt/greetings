package net.bakaar.greetings.stat.rest;

import net.bakaar.greetings.stat.application.StatApplicationService;
import net.bakaar.greetings.stat.domain.GreetingsStats;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebFluxTest(controllers = StatRestController.class)
@AutoConfigureWebClient
class StatRestControllerIT {

    @Autowired
    private WebTestClient client;

    @MockitoBean
    private StatApplicationService service;

    @Test
    void should_respond_json() {
        // Arrange
        var counters = Map.of("BIRTHDAY", 34L, "ANNIVERSARY", 89L);
        var stats = new GreetingsStats(counters);
        given(service.retrieveGreetingsStats()).willReturn(Mono.just(stats));
        // Act
        client.get()
                .uri("/rest/api/v1/stats")
                .accept(APPLICATION_JSON)
                .exchange()
                // Assert
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody().json("""
                        {
                            "counters" : {
                                "BIRTHDAY": 34,
                                "ANNIVERSARY": 89
                            }
                        }""");

    }

    @Test
    void should_respond_with_204() {
        // Arrange
        given(service.retrieveGreetingsStats()).willReturn(Mono.just(new GreetingsStats(new HashMap<>())));
        // Act
        client.get()
                .uri("/rest/api/v1/stats")
                .accept(APPLICATION_JSON)
                .exchange()
                // Assert
                .expectStatus().isNoContent();
    }
}
