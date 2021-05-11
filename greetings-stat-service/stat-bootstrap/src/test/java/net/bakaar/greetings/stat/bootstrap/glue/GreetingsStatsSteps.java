package net.bakaar.greetings.stat.bootstrap.glue;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.slf4j.Slf4j;
import net.bakaar.greetings.stat.message.GreetingMessage;
import net.bakaar.greetings.stat.persistence.CounterRepository;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.net.URI;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static net.bakaar.greetings.stat.bootstrap.CucumberLauncherIT.dbContainer;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@CucumberContextConfiguration
@EmbeddedKafka(partitions = 1)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@AutoConfigureWireMock(port = 0)
public class GreetingsStatsSteps {

    private final UUID identifier = UUID.randomUUID();
    private final String type = "ANNIVERSARY";
    private final String name = "Lucius";
    @Autowired
    // TODO replace that by a container
    private EmbeddedKafkaBroker embeddedKafka;
    @LocalServerPort
    private int port;
    @Value("${greetings.message.topic}")
    private String topic;

    @Autowired
    private CounterRepository counterRepository;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url",
                () -> String.format("r2dbc:postgresql://localhost:%d/%s",
                        dbContainer.getFirstMappedPort(), dbContainer.getDatabaseName()));
        registry.add("spring.r2dbc.password", dbContainer::getPassword);
        registry.add("spring.r2dbc.username", dbContainer::getUsername);
        registry.add("spring.flyway.url", () -> format("jdbc:postgresql://localhost:%d/%s",
                dbContainer.getFirstMappedPort(), dbContainer.getDatabaseName()));
        registry.add("spring.flyway.user", dbContainer::getUsername);
        registry.add("spring.flyway.password", dbContainer::getPassword);
        registry.add("greetings.stat.rest.client.url", () -> "http://localhost:${wiremock.server.port}/rest/api/v1/greetings");
    }

    @When("I create a greeting")
    public void i_create_a_greetings() {
        // send the message on the kafka topic
        var producerFactory = new DefaultKafkaProducerFactory<String, GreetingMessage>(
                KafkaTestUtils.producerProps(embeddedKafka));
        producerFactory.setKeySerializer(new StringSerializer());
        producerFactory.setValueSerializer(new JsonSerializer<>());
        var producer = producerFactory.createProducer();
        var message = new GreetingMessage(URI.create("http://bakaar.net/greetings/events/greeting-created"), """
                {
                   "identifier": "%s",
                   "raisedAt" : "2010-01-01T12:00:00+01:00"
                }
                """.formatted(identifier));
        producer.send(new ProducerRecord<>(topic, identifier.toString(), message));
        producer.flush();
        // Stub the answer from greetings service
        stubFor(get(urlEqualTo(format("/rest/api/v1/greetings/%s", identifier))).willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                        {
                            "type":"%s",
                            "name":"%s"
                        }
                        """.formatted(type, name))));
    }

    @Then("the counter should be {int}")
    public void the_counter_should_be(Integer counter) {
        await().until(() -> {
            var counterDb = counterRepository.findCounterByNameEquals(type).block();
            log.debug("Count = " + (counterDb != null ? counterDb.getCount() : 99));
            return counterDb != null && counterDb.getCount() > 0;
        });
        given().get(format("http://localhost:%d/rest/api/v1/stats", port))
                .then()
                .log().all(true)
                .statusCode(200)
                .contentType("application/json")
                // FIXME make it more precise...
                .body(containsString(format(":%d", counter)));
    }
}
