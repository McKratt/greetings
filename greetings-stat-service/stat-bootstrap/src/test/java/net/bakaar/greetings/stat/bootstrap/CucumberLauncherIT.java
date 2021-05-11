package net.bakaar.greetings.stat.bootstrap;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.testcontainers.containers.PostgreSQLContainer;

@RunWith(Cucumber.class)
@CucumberOptions(features = "../../bdd/features/GreetingsStats.feature",
        glue = "net.bakaar.greetings.stat.bootstrap.glue")
public class CucumberLauncherIT {
    @ClassRule
    public static final PostgreSQLContainer dbContainer = new PostgreSQLContainer("postgres")
            .withDatabaseName("stats")
            .withUsername("foo")
            .withPassword("secret");
}
