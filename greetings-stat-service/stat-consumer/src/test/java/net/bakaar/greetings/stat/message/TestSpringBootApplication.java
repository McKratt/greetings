package net.bakaar.greetings.stat.message;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(proxyBeanMethods = false, scanBasePackages = "net.bakaar.greetings.stat")
public class TestSpringBootApplication {

    @Bean
    NewTopic greetingTopic(GreetingsMessageProperties properties) {
        return new NewTopic(properties.getTopic(), 1, (short) 1);
    }
}
