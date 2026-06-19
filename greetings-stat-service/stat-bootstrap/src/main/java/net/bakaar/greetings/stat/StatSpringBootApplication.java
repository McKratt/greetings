package net.bakaar.greetings.stat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(proxyBeanMethods = false)
@EnableTransactionManagement
public class StatSpringBootApplication {
    static void main() {
        SpringApplication.run(StatSpringBootApplication.class);
    }
}
