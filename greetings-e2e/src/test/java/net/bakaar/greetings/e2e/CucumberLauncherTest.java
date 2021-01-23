package net.bakaar.greetings.e2e;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@RunWith(Cucumber.class)
@CucumberOptions(features = {
        "../bdd/features/GreetingsCreation.feature",
        "../bdd/features/GreetingsUpdate.feature"
},
        glue = "net.bakaar.greetings.e2e.glue",
        tags = "@e2e"
)
public class CucumberLauncherTest {

    public static final PostgreSQLContainer dbContainer = new PostgreSQLContainer("postgres")
            .withUsername("foo")
            .withPassword("secret");

    public static final GenericContainer serviceContainer = new GenericContainer("greeting-service")
            .withEnv("spring.datasource.username", dbContainer.getUsername())
            .withEnv("spring.datasource.password", dbContainer.getPassword());
}
