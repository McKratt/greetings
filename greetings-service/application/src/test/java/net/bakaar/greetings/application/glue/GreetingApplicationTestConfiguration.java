package net.bakaar.greetings.application.glue;

import net.bakaar.greetings.domain.Greeting;
import net.bakaar.greetings.domain.GreetingRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackages = "net.bakaar.greetings")
public class GreetingApplicationTestConfiguration {

    @Bean
    public GreetingRepository greetingRepository() {
        return new GreetingRepository() {
            private final Map<UUID, Greeting> greetings = new HashMap<>();

            @Override
            public Greeting put(Greeting greeting) {
                greetings.put(greeting.getIdentifier(), greeting);
                return greeting;
            }

            @Override
            public Optional<Greeting> find(UUID identifier) {
                return Optional.ofNullable(greetings.get(identifier));
            }
        };
    }
}
