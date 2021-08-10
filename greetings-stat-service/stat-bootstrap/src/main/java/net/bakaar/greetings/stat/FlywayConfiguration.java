package net.bakaar.greetings.stat;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(FlywayProperties.class)
public class FlywayConfiguration {

    @Bean(initMethod = "migrate")
    // @Lazy(value = false) FIXME Re enable once lazy loading fixed for Kafka consumer
    Flyway configureFlyway(FlywayProperties flywayProperties) {
        return new Flyway(Flyway.configure()
                .baselineOnMigrate(true)
                .dataSource(flywayProperties.getUrl(),
                        flywayProperties.getUser(),
                        flywayProperties.getPassword()));
    }
}
