package net.bakaar.greetings.stat.bootstrap.glue;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import net.bakaar.greetings.message.GreetingsMessage;
import net.bakaar.greetings.stat.persistence.Counter;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.CriteriaDefinition;
import org.springframework.data.relational.core.query.Query;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.net.URI;
import java.time.Duration;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static net.bakaar.greetings.stat.bootstrap.glue.BoostrapSpringCucumberContextConfiguration.greetings;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.containsString;

@Slf4j
public class GreetingsStatsSteps {

    public static final String topic = "test-topic";
    private final UUID identifier = UUID.randomUUID();
    private static Consumer<String, GreetingsMessage> consumer;
    private final String name = "Lucius";
    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;
    @LocalServerPort
    private int port;
    private String type = "ANNIVERSARY";
    @Autowired
    private R2dbcEntityTemplate template;

    @Given("the christmas greetings counter is equal to {int}")
    public void the_christmas_greetings_counter_is_equal_to(int counter) {
        var entity = new Counter();
        entity.setCount(counter);
        entity.setName("CHRISTMAS");
        template.insert(entity).block();
        var ety = template.selectOne(Query.query(CriteriaDefinition.from(Criteria.where("S_NAME").is("CHRISTMAS"))), Counter.class)
                .doOnError(err -> fail(err.getMessage()))
                .block();
        assertThat(ety.getCount()).isEqualTo(counter);
    }

    @When("I create a greeting")
    public void i_create_a_greetings() {
        i_create_a_greetings("");
    }

    @When("I create a {word} greeting")
    public void i_create_a_greetings(String inputType) {
        if (inputType != null && !inputType.trim().isBlank()) {
            this.type = inputType;
        }
        // send the message on the kafka topic
        var producerFactory = new DefaultKafkaProducerFactory<String, GreetingsMessage>(
                KafkaTestUtils.producerProps(embeddedKafka));
        producerFactory.setKeySerializer(new StringSerializer());
        producerFactory.setValueSerializer(new JsonSerializer<>());
        var producer = producerFactory.createProducer();
        var message = new GreetingsMessage(URI.create("https://bakaar.net/greetings/events/greeting-created"), """
                {
                   "identifier": "%s",
                   "raisedAt" : "2010-01-01T12:00:00+01:00"
                }
                """.formatted(identifier));
        producer.send(new ProducerRecord<>(topic, identifier.toString(), message));
        producer.flush();
        // Stub the answer from greetings service
        greetings.stubFor(get(urlEqualTo("/rest/api/v1/greetings/%s".formatted(identifier))).willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                        {
                            "type":"%s",
                            "name":"%s"
                        }
                        """.formatted(type, name))));
        // Check the message is in Topic with another groupId
        if (consumer == null) {
            consumer = createConsumer(); // It could only be one consumer
        }
        ConsumerRecord<String, GreetingsMessage> consumedRecord = KafkaTestUtils.getSingleRecord(consumer, topic, Duration.ofMillis(10000));
        var testMessage = consumedRecord.value();
        assertThat(testMessage).isNotNull();
        assertThat(testMessage.type()).isEqualTo(URI.create("https://bakaar.net/greetings/events/greeting-created"));
        assertThat(testMessage.payload()).contains(identifier.toString());
    }

    private Consumer<String, GreetingsMessage> createConsumer() {
        var consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", this.embeddedKafka);
        consumerProps.put(VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerProps.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "net.bakaar.*");
        var factory = new DefaultKafkaConsumerFactory<String, GreetingsMessage>(consumerProps);
        Consumer<String, GreetingsMessage> createdConsumer = factory.createConsumer();
        embeddedKafka.consumeFromAnEmbeddedTopic(createdConsumer, topic);
        return createdConsumer;
    }

    @Then("the counter should be {int}")
    public void the_counter_should_be(Integer counter) {
        await().until(() -> {
            var counterDb = template.selectOne(Query.query(CriteriaDefinition.from(Criteria.where("S_NAME").is(type.toUpperCase()))), Counter.class).block();
            log.debug(type + " counter = " + (counterDb != null ? counterDb.getCount() : "unknown"));
            return counterDb != null && counterDb.getCount() > 0;
        });
        given().get("http://localhost:%d/rest/api/v1/stats".formatted(port))
                .then()
                .log().all(true)
                .statusCode(200)
                .contentType("application/json")
                .body(containsString(":%d".formatted(counter)), containsString("\"%s\":".formatted(type.toUpperCase())));
    }
}
