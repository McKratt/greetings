package net.bakaar.greetings.servicetest.glue;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@EmbeddedKafka(partitions = 1, topics = CucumberSpringContextConfiguration.topic)
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CucumberSpringContextConfiguration {

    public static final String topic = "test-topic";

    @ServiceConnection
    static final PostgreSQLContainer dbContainer = new PostgreSQLContainer("postgres")
            .withDatabaseName("greetings")
            .withUsername("foo")
            .withPassword("secret");

    static {
        dbContainer.start();
    }

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("greetings.message.producer.topicName", () -> topic);
        registry.add("greetings.message.producer.numPartition", () -> 1);
        registry.add("greetings.message.producer.replication", () -> 1);
        registry.add("spring.kafka.bootstrap-servers", () -> "${spring.embedded.kafka.brokers}");
    }
}
