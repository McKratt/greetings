package net.bakaar.greetings.stat.bootstrap.glue;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.cucumber.java.AfterAll;
import io.cucumber.spring.CucumberContextConfiguration;
import net.bakaar.greetings.stat.StatSpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.postgresql.PostgreSQLContainer;

import static net.bakaar.greetings.stat.bootstrap.glue.GreetingsStatsSteps.TOPIC;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@CucumberContextConfiguration
@EmbeddedKafka(partitions = 1, topics = TOPIC)
@SpringBootTest(classes = {StatSpringBootApplication.class}, webEnvironment = RANDOM_PORT, properties = {
        "spring.profiles.active=test"
})
@ImportTestcontainers
public class BoostrapSpringCucumberContextConfiguration {

    public static final WireMockServer greetings = new WireMockServer(0);

    @ServiceConnection
    private static final PostgreSQLContainer dbContainer = new PostgreSQLContainer("postgres")
            .withDatabaseName("stats")
            .withUsername("foo")
            .withPassword("secret");

    static {
        dbContainer.start();
        greetings.start();
    }

    @AfterAll
    public static void afterAll() {
        dbContainer.stop();
        greetings.stop();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.flyway.url", dbContainer::getJdbcUrl);
        registry.add("spring.flyway.user", dbContainer::getUsername);
        registry.add("spring.flyway.password", dbContainer::getPassword);
        registry.add("greetings.message.topic", () -> TOPIC);
        registry.add("spring.kafka.bootstrap-servers", () -> "${spring.embedded.kafka.brokers}");
        registry.add("greetings.stat.rest.client.url", () -> "http://localhost:" + greetings.port());
    }
}
