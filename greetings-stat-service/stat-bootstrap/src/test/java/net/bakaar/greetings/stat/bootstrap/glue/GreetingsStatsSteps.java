package net.bakaar.greetings.stat.bootstrap.glue;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import net.bakaar.greetings.stat.message.GreetingMessage;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@CucumberContextConfiguration
@EmbeddedKafka(partitions = 1)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles(profiles = "test")
//@AutoConfigureWireMock(port = 0)
public class
GreetingsStatsSteps {

    private final UUID identifier = UUID.randomUUID();
    @Autowired
    // TODO replace that by a container
    private EmbeddedKafkaBroker embeddedKafka;

    @LocalServerPort
    private int port;

    @Value("${greetings.message.topic}")
    private String topic;

    @When("I create a greeting")
    public void i_create_a_greetings() {
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
        //        stubFor(get(urlEqualTo(format("/rest/api/v1/greetings/%s", identifier))).willReturn(aResponse()
//                .withStatus(200)
//                .withHeader("Content-Type", "application/json")
//                .withBody("""
//                        {
//                            "type":"%s",
//                            "name":"%s"
//                        }
//                        """.formatted(type, name))));
    }

    @Then("the counter should be {int}")
    public void the_counter_should_be(Integer counter) {
        given().get(format("http://localhost:%d/greetings/rest/api/v1/stats", port))
                .then()
                .log().all(true)
                // FIXME make it more precise...
                .body(containsString(format(":\"%d\"", counter)));
    }
}
