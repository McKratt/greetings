package net.bakaar.greetings.servicetest.glue;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import net.bakaar.greetings.message.producer.GreetingProducerProperties;
import net.bakaar.greetings.message.producer.GreetingsMessage;
import net.bakaar.greetings.persist.GreetingJpaEntity;
import net.bakaar.greetings.persist.GreetingJpaRepository;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static net.bakaar.greetings.servicetest.CucumberLauncherIT.dbContainer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@EmbeddedKafka(partitions = 1)
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
    private final Map<String, URI> TYPE_MAP = Map.of("GreetingCreated", URI.create("https://bakaar.net/greetings/events/greeting-created"));
    @Autowired
    // TODO replace that by a container
    private EmbeddedKafkaBroker embeddedKafka;
    @Autowired
    private GreetingProducerProperties messageProperties;

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",
                () -> String.format("jdbc:postgresql://localhost:%d/%s",
                        dbContainer.getFirstMappedPort(), dbContainer.getDatabaseName()));
        registry.add("spring.datasource.username", dbContainer::getUsername);
        registry.add("spring.datasource.password", dbContainer::getPassword);
        registry.add("greetings.message.producer.topicName", () -> "test_topic");
        registry.add("spring.kafka.bootstrap-servers", () -> "${spring.embedded.kafka.brokers}");
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

    @Then("an event {word} is emitted")
    public void an_event_is_emitted(String eventType) {
        var consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", this.embeddedKafka);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        var factory = new DefaultKafkaConsumerFactory<String, GreetingsMessage>(consumerProps);
        factory.setKeyDeserializer(new StringDeserializer());
        factory.setValueDeserializer(new JsonDeserializer<>());
        Consumer<String, GreetingsMessage> consumer = factory.createConsumer();
        ConsumerRecord<String, GreetingsMessage> record = KafkaTestUtils.getSingleRecord(consumer, messageProperties.getTopicName(), 10000L);
        var message = record.value();
        assertThat(message).isNotNull();
        assertThat(message.type()).isEqualTo(TYPE_MAP.get(eventType));
        assertThat(message.payload()).contains(identifier);
    }

    @Then("I get an error")
    // TODO put the status code to 400 once the error handling done
    public void iGetAnError() {
        assertThat(response.statusCode()).isEqualTo(500);
    }

    @Then("the greeting is now a {word} one")
    public void the_greeting_is_now_a_new_type_one(String type) {
        response.then().log().everything(true).body("message", Matchers.containsStringIgnoringCase(type));
    }
}
