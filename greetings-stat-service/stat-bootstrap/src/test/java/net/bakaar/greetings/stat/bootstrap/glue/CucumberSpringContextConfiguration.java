package net.bakaar.greetings.stat.bootstrap.glue;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.cucumber.java.AfterAll;
import io.cucumber.spring.CucumberContextConfiguration;
import net.bakaar.greetings.stat.StatSpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import static net.bakaar.greetings.stat.bootstrap.glue.GreetingsStatsSteps.topic;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@CucumberContextConfiguration
@EmbeddedKafka(partitions = 1, topics = topic)
@SpringBootTest(classes = {StatSpringBootApplication.class}, webEnvironment = RANDOM_PORT, properties = {
        "spring.profiles.active=test"
})
public class CucumberSpringContextConfiguration {

    public static final WireMockServer greetings = new WireMockServer(0);

    private static final PostgreSQLContainer dbContainer = new PostgreSQLContainer("postgres")
            .withDatabaseName("stats")
            .withUsername("foo")
            .withPassword("secret");


    static {
        dbContainer.start();
        greetings.start();
    }

    @AfterAll
    static void afterAll() {
        dbContainer.stop();
        greetings.stop();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url",
                () -> String.format("r2dbc:postgresql://localhost:%d/%s",
                        dbContainer.getFirstMappedPort(), dbContainer.getDatabaseName()));
        registry.add("spring.r2dbc.password", dbContainer::getPassword);
        registry.add("spring.r2dbc.username", dbContainer::getUsername);
        registry.add("spring.flyway.url", dbContainer::getJdbcUrl);
        registry.add("spring.flyway.user", dbContainer::getUsername);
        registry.add("spring.flyway.password", dbContainer::getPassword);
        registry.add("greetings.message.topic", () -> topic);
        registry.add("spring.kafka.bootstrap-servers", () -> "${spring.embedded.kafka.brokers}");
        registry.add("greetings.stat.rest.client.url", () -> "http://localhost:" + greetings.port());
    }
}
