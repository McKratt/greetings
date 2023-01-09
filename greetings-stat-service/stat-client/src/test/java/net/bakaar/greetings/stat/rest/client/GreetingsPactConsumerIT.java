package net.bakaar.greetings.stat.rest.client;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.util.UUID;

import static java.util.Collections.singletonMap;

@RestClientTest
@ExtendWith({PactConsumerTestExt.class})
@PactTestFor(providerName = "greetings-service", pactVersion = PactSpecVersion.V3)
@ContextConfiguration(classes = GreetingsStatRestClientConfiguration.class)
class GreetingsPactConsumerIT {

    private final String stringIdentifier = "03e805ff-5860-49a6-88bc-a1dcda0dd4b4";
    private final UUID identifier = UUID.fromString(stringIdentifier);
    @Autowired
    private WebClient.Builder builder;


    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("greetings.stat.rest.client.url", () -> "http://localhost");
    }

    @Pact(consumer = "greetings-stat-service")
    public RequestResponsePact pactForGetAGreeting(PactDslWithProvider builder) {
        return builder
                .given("An existing Greeting id " + stringIdentifier)
                .uponReceiving("Get a greeting for Name and type")
                .path("/rest/api/v1/greetings/" + stringIdentifier)
                .method("GET")
                .headers("Accept", "application/json")
                .willRespondWith()
                .status(200)
                .body(new PactDslJsonBody()
                        .stringMatcher("name", "[A-Z].*", "Fermi")
                        .stringMatcher("type", "[A-Z]+", "CHRISTMAS")
                )
                .headers(singletonMap("Content-Type", "application/json"))
                .toPact();
    }

    @Test
    void should_read_greetings_from_pact(MockServer mockServer) {
        var client = new GreetingsRepositoryAdapter(WebClient.builder().baseUrl(mockServer.getUrl()).build());
        StepVerifier
                .create(client.getGreetingForIdentifier(identifier))
                .expectNextCount(1)
                .verifyComplete();
    }
}
