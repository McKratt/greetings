package net.bakaar.greetings.rest;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;

/**
 * Spring-Boot 2.4.X doesn't support Jackson 2.12 and then doesn't Support Record.
 * We have to wait Spring-Boot 2.5.
 * see https://github.com/spring-projects/spring-boot/issues/20831
 * and https://github.com/spring-projects/spring-boot/pull/24415
 */
@Configuration
public class JsonSerializerConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> builder.visibility(FIELD, ANY);
    }
}
