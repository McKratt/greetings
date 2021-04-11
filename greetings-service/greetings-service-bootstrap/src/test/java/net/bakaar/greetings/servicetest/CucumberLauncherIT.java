package net.bakaar.greetings.servicetest;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.testcontainers.containers.PostgreSQLContainer;

@RunWith(Cucumber.class)
@CucumberOptions(features = {
        "../../bdd/features"
},
        glue = "net.bakaar.greetings.servicetest.glue",
        tags = "not @stat")
public class CucumberLauncherIT {

    @ClassRule
    public static final PostgreSQLContainer dbContainer = new PostgreSQLContainer("postgres")
            .withDatabaseName("greetings")
            .withUsername("foo")
            .withPassword("secret");
}
