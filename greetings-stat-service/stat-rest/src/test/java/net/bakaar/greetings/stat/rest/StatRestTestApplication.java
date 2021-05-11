package net.bakaar.greetings.stat.rest;

import net.bakaar.greetings.stat.domain.GreetingsStats;
import net.bakaar.greetings.stat.domain.StatRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;

@SpringBootApplication(proxyBeanMethods = false,
        scanBasePackages = "net.bakaar.greetings.stat")
public class StatRestTestApplication {

    @Bean
    StatRepository statRepository() {
        return new StatRepository() {
            private GreetingsStats stats = new GreetingsStats(new HashMap<>());

            @Override
            public void put(GreetingsStats stats) {
                this.stats = stats;
            }

            @Override
            public GreetingsStats pop() {
                return stats;
            }
        };
    }
}
