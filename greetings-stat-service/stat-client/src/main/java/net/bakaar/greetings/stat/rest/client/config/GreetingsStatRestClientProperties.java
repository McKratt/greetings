package net.bakaar.greetings.stat.rest.client.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "greetings.stat.rest.client")
public class GreetingsStatRestClientProperties {
    private String url;
}
