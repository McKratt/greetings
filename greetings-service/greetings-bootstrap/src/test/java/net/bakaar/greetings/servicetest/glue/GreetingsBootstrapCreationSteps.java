package net.bakaar.greetings.servicetest.glue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import net.bakaar.greetings.message.GreetingsMessage;
import net.bakaar.greetings.message.producer.GreetingsProducerProperties;
import net.bakaar.greetings.rest.IdentifiedGreetingMessage;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static net.bakaar.greetings.servicetest.glue.CucumberSpringContextConfiguration.topic;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;

@Slf4j

public class GreetingsBootstrapCreationSteps {

    private final String identifier = UUID.randomUUID().toString();
    private final RequestSpecification request = given()
            .log().all(true)
            .contentType("application/json")
            .accept("application/json");
    private Response response;
    @LocalServerPort
    private int port;
    @Autowired
    private JdbcTemplate dbTemplate;
    @Autowired
    // FIXME replace that by a container
    private EmbeddedKafkaBroker embeddedKafka;
    @Autowired
    private GreetingsProducerProperties messageProperties;
    @Autowired
    private KafkaAdmin kafkaAdmin;
    @Autowired
    private ObjectMapper jsonMapper;


    @Given("an existing {word} greeting")
    public void an_existing_greeting(String type) {
        var idType = dbTemplate.queryForObject("select pk_t_types from t_types where s_name = ?", Long.class, type.toUpperCase(Locale.ROOT));
        dbTemplate.update("insert into t_greetings (s_identifier, s_name, fk_type, ts_createdat) values (?,?,?,?)",
                identifier, "Koala", idType, LocalDateTime.now());
    }

    @When("I create a(n) {word} greeting for {word}")
    public void iCreateAGreetingForName(String type, String name) {
        await().until(() -> !kafkaAdmin.describeTopics(topic).isEmpty());
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

    @Then("a Greeting is created")
    public void a_greeting_is_created() throws JsonProcessingException {
        Consumer<String, GreetingsMessage> consumer = createConsumer();
        ConsumerRecord<String, GreetingsMessage> record = KafkaTestUtils.getSingleRecord(consumer, messageProperties.getTopicName(), Duration.ofMillis(10000));
        var message = record.value();
        assertThat(message).isNotNull();
        assertThat(message.type()).isEqualTo(URI.create("https://bakaar.net/greetings/events/greeting-created"));
        var uuid = jsonMapper.readValue(response.getBody().asString(), IdentifiedGreetingMessage.class).id();
        assertThat(uuid).isNotEmpty();
        assertThat(message.payload()).contains(uuid);
    }

    @NotNull
    private Consumer<String, GreetingsMessage> createConsumer() {
        var consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", this.embeddedKafka);
        consumerProps.put(VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerProps.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "net.bakaar.*");
        var factory = new DefaultKafkaConsumerFactory<String, GreetingsMessage>(consumerProps);
        Consumer<String, GreetingsMessage> consumer = factory.createConsumer();
        embeddedKafka.consumeFromAnEmbeddedTopic(consumer, topic);
        return consumer;
    }

    @Then("I get an error")
    // FIXME put the status code to 400 once the error handling done
    public void iGetAnError() {
        assertThat(response.statusCode()).isEqualTo(500);
    }

    @Then("the greeting is now a {word} one")
    public void the_greeting_is_now_a_new_type_one(String type) {
        // TODO do the update event
//        Consumer<String, GreetingsMessage> consumer = createConsumer();
//        ConsumerRecord<String, GreetingsMessage> record = KafkaTestUtils.getSingleRecord(consumer, messageProperties.getTopicName(), 10000L);
//        var message = record.value();
//        assertThat(message).isNotNull();
//        assertThat(message.type()).isEqualTo(URI.create("https://bakaar.net/greetings/events/greeting-updated"));
        response.then().log().everything(true).body("message", Matchers.containsStringIgnoringCase(type));
    }
}
