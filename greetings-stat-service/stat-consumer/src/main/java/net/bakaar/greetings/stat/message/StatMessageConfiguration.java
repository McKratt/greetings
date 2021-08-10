package net.bakaar.greetings.stat.message;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.bakaar.greetings.stat.application.StatApplicationService;
import net.bakaar.greetings.stat.message.handler.CreatedGreetingEventPayloadHandler;
import net.bakaar.greetings.stat.message.handler.GreetingMessagePayloadHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.listener.ContainerStoppingErrorHandler;
import org.springframework.kafka.listener.ErrorHandler;

@EnableKafka
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(GreetingsMessageProperties.class)
@PropertySource("classpath:config/customer.properties")
public class StatMessageConfiguration {

    @Bean
    ErrorHandler errorHandler() {
        return new ContainerStoppingErrorHandler();
    }

    @Bean
    GreetingMessagePayloadHandler greetingCreatedPayloadHandler(StatApplicationService service) {
        return new CreatedGreetingEventPayloadHandler(service, createJsonMapper());
    }

    private ObjectMapper createJsonMapper() {
        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }
}
