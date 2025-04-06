package net.bakaar.greetings.stat.message.glue;

import io.cucumber.spring.CucumberContextConfiguration;
import net.bakaar.greetings.stat.application.GreetingsRepository;
import net.bakaar.greetings.stat.domain.StatRepository;
import net.bakaar.greetings.stat.message.TestSpringBootApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@CucumberContextConfiguration
@EmbeddedKafka(partitions = 1)
@SpringBootTest(classes = TestSpringBootApplication.class, properties = {
        "logging.level.kafka=WARN"
})
public class ConsumerSpringCucumberContextConfiguration {
    @Value("${greetings.message.topic}")
    public static final String topic = "stat_topic";
    @MockitoBean
    private GreetingsRepository greetingsRepository;
    @MockitoBean
    private StatRepository statRepository;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("greetings.message.topic", () -> topic);
        registry.add("spring.kafka.bootstrap-servers", () -> "${spring.embedded.kafka.brokers}");
        registry.add("greetings.stat.rest.client.url", () -> "http://localhost:${wiremock.server.port}/rest/api/v1/greetings");
    }
}
