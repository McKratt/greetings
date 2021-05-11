package net.bakaar.greetings.stat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(proxyBeanMethods = false)
public class StatSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(StatSpringBootApplication.class);
    }
}
