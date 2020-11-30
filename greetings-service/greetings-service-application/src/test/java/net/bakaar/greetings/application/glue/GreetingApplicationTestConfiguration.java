package net.bakaar.greetings.application.glue;

import net.bakaar.greetings.domain.Greeting;
import net.bakaar.greetings.domain.GreetingRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "net.bakaar.greetings")
public class GreetingApplicationTestConfiguration {

    @Bean
    public GreetingRepository greetingRepository() {
        return new GreetingRepository() {
            @Override
            public Greeting put(Greeting greeting) {
                return greeting;
            }
        };
    }
}
