package net.bakaar.greetings.stat.persistence;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration(proxyBeanMethods = false)
@PropertySource("classpath:persistence.properties")
public class PersistenceConfiguration {
}
