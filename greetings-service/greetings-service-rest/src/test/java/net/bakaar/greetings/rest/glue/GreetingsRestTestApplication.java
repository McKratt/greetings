package net.bakaar.greetings.rest.glue;

import net.bakaar.greetings.domain.GreetingRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "net.bakaar.greetings")
public class GreetingsRestTestApplication {

    @Bean
    GreetingRepository repository() {
        return greeting -> greeting;
    }
}
