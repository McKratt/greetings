package net.bakaar.greetings.stat.message;

import lombok.Getter;
import net.bakaar.greetings.stat.application.StatApplicationService;
import net.bakaar.greetings.stat.domain.GreetingCreated;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

import java.util.UUID;

@SpringBootApplication(scanBasePackages = "net.bakaar.greetings.stat")
public class TestSpringBootApplication {

    @Bean
    @Primary
    StatApplicationService testApplicationService() {
        return new TestApplicationService();
    }

    @Bean
    NewTopic greetingTopic(GreetingsMessageProperties properties) {
        return new NewTopic(properties.getTopic(), 1, (short) 1);
    }

    @Getter
    public static class TestApplicationService extends StatApplicationService {
        private int counter = 0;
        private UUID lastIdentifier;

        @Override
        public Mono<Void> handle(GreetingCreated event) {
            counter++;
            lastIdentifier = event.identifier();
            return Mono.empty();
        }
    }
}
