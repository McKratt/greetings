package net.bakaar.greetings.message.producer;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "greetings.message.producer")
public class GreetingsProducerProperties {

    private String topicName;
}
