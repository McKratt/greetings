package net.bakaar.greetings.stat.message;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "greetings.message")
public class GreetingsMessageProperties {
    private String topic;
}
