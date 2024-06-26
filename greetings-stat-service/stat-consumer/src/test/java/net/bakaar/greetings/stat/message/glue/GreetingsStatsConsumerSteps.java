package net.bakaar.greetings.stat.message.glue;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.bakaar.greetings.message.GreetingsMessage;
import net.bakaar.greetings.stat.application.GreetingsRepository;
import net.bakaar.greetings.stat.application.readmodel.Greeting;
import net.bakaar.greetings.stat.domain.GreetingsStats;
import net.bakaar.greetings.stat.domain.StatRepository;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static net.bakaar.greetings.stat.message.glue.ConsumerSpringCucumberContextConfiguration.topic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.BDDMockito.given;


public class GreetingsStatsConsumerSteps {
    private final UUID identifier = UUID.randomUUID();
    private String type = "birthday";
    private final String name = "Albert";
    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;
    @Autowired
    private StatRepository statRepository;
    @Autowired
    private GreetingsRepository greetingsRepository;
    private GreetingsStats mockedStats = new GreetingsStats(new HashMap<>(Map.of("BIRTHDAY", 0L, "ANNIVERSARY", 0L, "CHRISTMAS", 0L)));

    @Given("the christmas greetings counter is equal to {long}")
    public void the_christmas_greetings_counter_is_equal_to(long count) {
        mockedStats = new GreetingsStats(new HashMap<>(Map.of("CHRISTMAS", count)));
    }

    @When("I create a {word} greeting")
    public void i_create_a_greeting(String inputType) {
        if (inputType != null && !inputType.trim().isBlank()) {
            this.type = inputType;
        }
        given(statRepository.pop()).willReturn(CompletableFuture.completedFuture(mockedStats));
        // Mock the call to greeting service
        var greeting = new Greeting(this.type, name);
        given(greetingsRepository.getGreetingForIdentifier(identifier)).willReturn(Mono.just(greeting));
        // send the event on Kafka
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

    }


    @When("I create a greeting")
    public void i_create_a_greetings() {
        i_create_a_greeting("");
    }

    @Then("the counter should be {long}")
    public void the_counter_should_be(Long counter) throws ExecutionException, InterruptedException {
        await().until(() -> statRepository.pop().thenApply(pop -> pop.getStatsFor(type).get().equals(counter)).get());
        var stats = statRepository.pop();
        assertThat(stats).isNotNull();
        assertThat(stats.get().getStatsFor(type)).isPresent().get().isEqualTo(counter);
    }
}
