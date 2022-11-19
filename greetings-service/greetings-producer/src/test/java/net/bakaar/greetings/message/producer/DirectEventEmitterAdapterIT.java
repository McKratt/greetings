package net.bakaar.greetings.message.producer;

import net.bakaar.greetings.domain.Greeting;
import net.bakaar.greetings.domain.event.EventEmitter;
import net.bakaar.greetings.domain.event.GreetingCreated;
import net.bakaar.greetings.message.GreetingsMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.net.URI;
import java.time.Duration;
import java.util.UUID;

import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@EmbeddedKafka
@SpringJUnitConfig(classes = GreetingsProducerConfiguration.class)
@EnableAutoConfiguration
class DirectEventEmitterAdapterIT {

    private static final String topicName = "test_topic";
    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Autowired
    private EventEmitter emitter;

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", () -> "${spring.embedded.kafka.brokers}");
        registry.add("greetings.message.producer.topicName", () -> topicName);
    }

    @Test
    void should_send_createdEvent_to_kafka() {
        // Given
        var identifier = UUID.randomUUID();
        var greeting = mock(Greeting.class);
        given(greeting.getIdentifier()).willReturn(identifier);
        embeddedKafka.addTopics(topicName);
        // When
        var event = GreetingCreated.of(greeting);
        emitter.emit(event);
        // Then
        var consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", this.embeddedKafka);
        consumerProps.put(VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerProps.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "net.bakaar.*");
        var factory = new DefaultKafkaConsumerFactory<String, GreetingsMessage>(consumerProps);
        var consumer = factory.createConsumer();
        embeddedKafka.consumeFromAnEmbeddedTopic(consumer, topicName);
        ConsumerRecord<String, GreetingsMessage> record = KafkaTestUtils.getSingleRecord(consumer, topicName, Duration.ofMillis(10000));
        var receivedMessage = record.value();
        assertThat(receivedMessage).isNotNull();
        assertThat(receivedMessage.type()).isEqualTo(URI.create("https://bakaar.net/greetings/events/greeting-created"));
        assertThat(receivedMessage.payload()).contains(identifier.toString());
    }
}
