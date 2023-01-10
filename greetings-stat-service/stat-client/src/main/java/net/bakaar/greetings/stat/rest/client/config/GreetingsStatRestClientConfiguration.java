package net.bakaar.greetings.stat.rest.client.config;

import net.bakaar.greetings.stat.application.GreetingsRepository;
import net.bakaar.greetings.stat.rest.client.GreetingsRepositoryAdapter;
import net.bakaar.greetings.stat.rest.client.GreetingsRestClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@EnableConfigurationProperties(GreetingsStatRestClientProperties.class)
public class GreetingsStatRestClientConfiguration {

    @Bean
    GreetingsRestClient client(GreetingsStatRestClientProperties properties, WebClient.Builder builder) {
        WebClient client = builder.baseUrl(properties.getUrl()).build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(client)).build();

        return factory.createClient(GreetingsRestClient.class);
    }

    @Bean
    GreetingsRepository greetingsRepository(GreetingsRestClient client) {
        return new GreetingsRepositoryAdapter(client);
    }
}
