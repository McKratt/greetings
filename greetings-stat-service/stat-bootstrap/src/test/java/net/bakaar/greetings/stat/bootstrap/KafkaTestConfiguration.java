package net.bakaar.greetings.stat.bootstrap;

import net.bakaar.greetings.stat.message.GreetingsMessageProperties;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTestConfiguration {
    @Bean
    NewTopic greetingTopic(GreetingsMessageProperties properties) {
        return new NewTopic(properties.getTopic(), 1, (short) 1);
    }
}
