package net.bakaar.greetings.persist;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * We need a SpringBootApplication somewhere to be able to use @DataJpaTest.
 */
@SpringBootApplication(proxyBeanMethods = false)
public class GreetingsPersistTestApplication {
}
