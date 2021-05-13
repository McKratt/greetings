package net.bakaar.greetings.message.producer;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GreetingProducerProperties.class)
public class GreetingsProducerConfiguration {

    @Bean
    NewTopic greetingTopic(GreetingProducerProperties properties) {
        return new NewTopic(properties.getTopicName(), 1, (short) 1);
    }
}
